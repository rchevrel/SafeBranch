package net.rchevrel.safebranch.branchgraph.business;

import net.rchevrel.safebranch.branchgraph.SafeBranchGraph;
import net.rchevrel.safebranch.gitreader.SafeRepository;
import net.rchevrel.safebranch.gitreader.business.GroupToBranchesBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class SafeRepository2SafeBranchGraph {

    private final SafeRepository safeRepository;

    public SafeRepository2SafeBranchGraph(SafeRepository safeRepository) {
        this.safeRepository = safeRepository;
    }

    public SafeBranchGraph createSafeBranchGraph(SafeBranchGraph safeBranchGraph) {
        // detect group of branches
        System.out.print("detect group of branches");
        Map<String, List<String>> group2branches = new GroupToBranchesBuilder(safeRepository).getGroupToBranches();
        System.out.println("Done");

        // prune the branch to commit id by replacing duplicate with group (commitId -> commitId)
        System.out.print("Detect group of branches : ");
        Map<String, String> branchOrGroupToCommitId =
                getBranchOrGroupToCommitId(safeRepository.getBranch2commitId(), group2branches);
        System.out.println("Done");

        // associate branch with all its parents
        System.out.print("Associate branch with all its parents : ");
        Map<String, List<String>> branchToParentBranches = getBranchToParentBranches(branchOrGroupToCommitId);
        System.out.println("Done");

        // prune branch for retains only first parent
        System.out.print("Prune branch for retains only first parent : ");
        Map<String, List<String>> prunedBranchToParentBranches = getBranchToParentBranches(branchOrGroupToCommitId);
        branchToParentBranches.keySet().forEach(branch -> {
            List<String> prunedParents = new ArrayList<>(branchToParentBranches.get(branch));
            List<String> parents = branchToParentBranches.get(branch);
            parents.forEach(parent -> prunedParents.removeAll(branchToParentBranches.get(parent)));
            prunedBranchToParentBranches.put(branch, prunedParents);
        });
        System.out.println("Done");
        // reverse parent to child

        prunedBranchToParentBranches.keySet().forEach(branch -> {
            List<String> prunedParentBranches = prunedBranchToParentBranches.get(branch);
            prunedParentBranches.stream().map(parent -> parent.replace("refs/remotes/origin/", "")).forEach(parent ->
                    System.out.println(parent + " --> " + branch.replace("refs/remotes/origin/", "")));
        });

        // Create graph

        return safeBranchGraph;
    }

    private Map<String, List<String>> getBranchToParentBranches(Map<String, String> branchOrGroupToCommitId) {
        Map<String, List<String>> branch2parents = new HashMap<>();
        Map<String, String> commitId2branch = branchOrGroupToCommitId.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        branchOrGroupToCommitId.keySet().forEach(branch -> {
            branch2parents.put(branch, new ArrayList<>());
            String commitId = branchOrGroupToCommitId.get(branch);
            List<String> allCommits = safeRepository.getAllCommits(commitId);
            allCommits.remove(commitId); // remove self
            allCommits.forEach(parentCommitId -> {
                if (commitId2branch.containsKey(parentCommitId)) {
                    branch2parents.get(branch).add(commitId2branch.get(parentCommitId));
                }
            });
        });
        return branch2parents;
    }

    private static Map<String, String> getBranchOrGroupToCommitId(Map<String, String> branch2commitId, Map<String, List<String>> group2branches) {
        Set<String> groupedBranches = group2branches.values().stream().flatMap(List::stream).collect(Collectors.toSet());
        Map<String, String> branchOrGroupToCommitId = branch2commitId.entrySet().stream()
                .filter(e -> !groupedBranches.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        group2branches.keySet().forEach(commitId -> branchOrGroupToCommitId.put(commitId, commitId));
        return branchOrGroupToCommitId;
    }


}
