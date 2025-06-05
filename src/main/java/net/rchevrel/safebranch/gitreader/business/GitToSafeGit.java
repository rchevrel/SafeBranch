package net.rchevrel.safebranch.gitreader.business;

import net.rchevrel.safebranch.gitreader.model.SafeCommit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitToSafeGit {

    private final Git git;

    public GitToSafeGit(Git git) {
        this.git = git;
    }

    public Map<String, SafeCommit> createSafeCommitTree() throws GitAPIException, IOException {
        System.out.println("## Create commit tree");
        System.out.print("Get all commit : ");
        Iterable<RevCommit> commandCommits = git.log().all().call();
        System.out.println("Done");
        List<RevCommit> commits = new ArrayList<>();
        commandCommits.forEach(commits::add);
        System.out.print("Create SafeCommit");
        Map<String, SafeCommit> safeCommitMap = createCommits(commits);
        System.out.println("Done");
        System.out.print("Create parent links : ");
        linkParents(commits, safeCommitMap);
        System.out.println("Done");
        return safeCommitMap;
    }

    private Map<String, SafeCommit> createCommits(List<RevCommit> commandCommits) {
        Map<String, SafeCommit> commitId2safeCommit = new HashMap<>();
        for (RevCommit commit : commandCommits) {
            commitId2safeCommit.put(commit.getName(), new SafeCommit(
                    commit.getName(),
                    commit.getFullMessage(),
                    commit.getFirstMessageLine(),
                    commit.getShortMessage()
            ));
        }
        return commitId2safeCommit;
    }

    private void linkParents(List<RevCommit> commandCommits, Map<String, SafeCommit> commitId2safeCommit) {
        for (RevCommit commit : commandCommits) {
            RevCommit[] parents = commit.getParents();
            if (parents.length != 0) {
                SafeCommit childSafeCommit = commitId2safeCommit.get(commit.getName());
                for (int index = 0; index < parents.length; index++) {
                    SafeCommit parentSafeCommit = commitId2safeCommit.get(parents[index].getName());
                    childSafeCommit.getParents().add(parentSafeCommit);
                }
            }
        }
    }


    public Map<String, String> createSafeBranches() throws GitAPIException {
        System.out.print("Create Safe Branches");
        Map<String, String> branch2commitId = new HashMap<>();
        List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
        for (Ref branch : branches) {
            branch2commitId.put(branch.getName(), branch.getObjectId().name());
        }
        System.out.println("Done");
        return branch2commitId;
    }

    public Map<String, String> createSafeTags() throws GitAPIException {
        System.out.print("Create Safe Tags");
        Map<String, String> tag2commitId = new HashMap<>();
        List<Ref> tags = git.tagList().call();
        for (Ref tag : tags) {
            tag2commitId.put(tag.getName(), tag.getObjectId().name());
        }
        System.out.println("Done");
        return tag2commitId;
    }
}
