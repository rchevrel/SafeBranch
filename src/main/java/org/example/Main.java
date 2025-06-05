package org.example;

import net.rchevrel.safebranch.branchgraph.SafeBranchGraph;
import net.rchevrel.safebranch.gitreader.SafeRepository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final String USER = "rchevrel";

    private static final String TOKEN = "<YourToken>";

    public static void main(String[] args) throws GitAPIException, IOException {
        File outRepo = Files.createTempDirectory("Git").toFile();
        outRepo.deleteOnExit();
        System.out.println(outRepo.getAbsolutePath());
        System.out.print("Cloning Repository : ");
        Git git = Git.cloneRepository()
                .setURI("https://github.com/microsoft/qlib.git")
                // .setURI("https://github.com/apache/hadoop.git")
                .setDirectory(outRepo)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(USER, TOKEN))
                .call();
        System.out.println("Done");
        SafeRepository safeRepository = SafeRepository.createAndParseGit(git);
        SafeBranchGraph safeBranchGraph = SafeBranchGraph.createSafeBranchGraph(safeRepository);

    }
}