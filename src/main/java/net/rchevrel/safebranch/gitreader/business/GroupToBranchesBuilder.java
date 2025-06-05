package net.rchevrel.safebranch.gitreader.business;

import net.rchevrel.safebranch.gitreader.SafeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupToBranchesBuilder {

    private final SafeRepository safeRepository;

    public GroupToBranchesBuilder(SafeRepository safeRepository) {
        this.safeRepository = safeRepository;
    }

    public Map<String, List<String>> getGroupToBranches() {
        Map<String, String> branch2commitId = safeRepository.getBranch2commitId();
        Map<String, List<String>> group2branches = new HashMap<>();
        branch2commitId.forEach((branch,commitId) -> {
            if (!group2branches.containsKey(commitId)) {
                group2branches.put(commitId, new ArrayList<>());
            }
            group2branches.get(commitId).add(branch);
        });
        return group2branches.entrySet().stream()
                .filter(this::retainOnlyMultipleBranchesGroup)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean retainOnlyMultipleBranchesGroup(Map.Entry<String, List<String>> e) {
        return e.getValue().size() > 1;
    }
}
