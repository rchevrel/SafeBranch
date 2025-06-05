package net.rchevrel.safebranch.branchgraph;

import net.rchevrel.safebranch.branchgraph.business.SafeRepository2SafeBranchGraph;
import net.rchevrel.safebranch.branchgraph.model.SafeNode;
import net.rchevrel.safebranch.gitreader.SafeRepository;

public class SafeBranchGraph extends SafeNode {

    private SafeBranchGraph() {
    }

    public static SafeBranchGraph createSafeBranchGraph(SafeRepository safeRepository) {
        return new SafeRepository2SafeBranchGraph(safeRepository).createSafeBranchGraph(new SafeBranchGraph());
    }
}
