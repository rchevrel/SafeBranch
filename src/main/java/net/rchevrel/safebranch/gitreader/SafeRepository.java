package net.rchevrel.safebranch.gitreader;

import net.rchevrel.safebranch.gitreader.business.GitToSafeGit;
import net.rchevrel.safebranch.gitreader.model.SafeCommit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.*;

public class SafeRepository {

    private final Map<String, SafeCommit> commitId2safeCommit;

    private final Map<String, String> branch2commitId;

    private final Map<String, String> tag2commitId;

    private SafeRepository(Map<String, SafeCommit> commitId2safeCommit, Map<String, String> branch2commitId, Map<String, String> tag2commitId) {
        this.commitId2safeCommit = commitId2safeCommit;
        this.branch2commitId = branch2commitId;
        this.tag2commitId = tag2commitId;
    }

    public static SafeRepository createAndParseGit(Git git) throws IOException, GitAPIException {
        GitToSafeGit gitToSafeGit = new GitToSafeGit(git);
        return new SafeRepository(
                gitToSafeGit.createSafeCommitTree(),
                gitToSafeGit.createSafeBranches(),
                gitToSafeGit.createSafeTags());
    }

    public List<String> getAllCommits(String commitId) {
        List<String> allCommits = new ArrayList<>();
        getAllCommits(commitId, allCommits);
        return allCommits;
    }

    private void getAllCommits(String commitId, List<String> allCommits) {
        if (allCommits.contains(commitId)) {
            return;
        }
        allCommits.add(commitId);
        SafeCommit safeCommit = commitId2safeCommit.get(commitId);
        safeCommit.getParents().forEach(parentSafeCommit -> getAllCommits(parentSafeCommit.getId(), allCommits));
    }

    public Map<String, String> getBranch2commitId() {
        return branch2commitId;
    }
}
