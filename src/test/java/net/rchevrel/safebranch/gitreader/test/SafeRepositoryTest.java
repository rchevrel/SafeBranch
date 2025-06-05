package net.rchevrel.safebranch.gitreader.test;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class SafeRepositoryTest {

    @Test
    void test1() throws IOException, GitAPIException {
//
//        unzip("F:/git/SafeBranch/src/test/resources/picam_flask-master.zip", outRepo.getAbsolutePath());

//        try (Git git = Git.open(outRepo)) {
        try (Git git = Git.open(new File("F:\\git\\SafeBranch\\src\\test\\resources\\picam_flask"))) {
            List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
            branches.forEach(System.out::println);
        }
    }

    @Test
    void test2() throws IOException, GitAPIException {
        File outRepo = Files.createTempDirectory("Git").toFile();
        File gitFile = new File(outRepo.getAbsolutePath() + File.separator + ".git");
        new FileRepositoryBuilder()
                .setGitDir(gitFile)
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .setMustExist(true)
                .build();
        Git git = Git.open(gitFile);
        List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
        branches.forEach(System.out::println);
    }

    @Test
    void test3() throws IOException, GitAPIException {
        File outRepo = Files.createTempDirectory("Git").toFile();
        Git git = Git.init().setDirectory(outRepo).call();
        outRepo.createNewFile();
        git.add().addFilepattern(".").call();
        git.commit().setMessage("Hello World").call();


        List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
        branches.forEach(System.out::println);
    }

    public static void main(String[] args) throws IOException, GitAPIException {
        File outRepo = Files.createTempDirectory("Git").toFile();
        System.out.println(outRepo.getAbsolutePath());
        Git git = Git.init().setDirectory(outRepo).call();
        outRepo.createNewFile();
        git.add().addFilepattern(".").call();
        git.commit().setMessage("Hello World").call();

        git.branchCreate().setName("feature/feat1").call();

        git.checkout().setName("feature/feat1").call();

        git.commit().setMessage("Hello World1").call();
        RevCommit helloWorld2 = git.commit().setMessage("Hello World2").call();

        git.checkout().setName("master").call();
        git.branchCreate().setName("feature/feat2").call();
        git.checkout().setName("feature/feat2").call();
        RevCommit helloWorld3 = git.commit().setMessage("Hello World3").call();
        git.commit().setMessage("Hello World4").call();

//
//        MergeResult mergeResult = git.merge().setMessage("Merge it").include(helloWorld2.getId()).call();
//        System.out.println(mergeResult.getMergeStatus());

        git.checkout().setName("master").call();
        git.branchCreate().setName("feature/feat3").call();
        git.checkout().setName("feature/feat3").call();
        git.commit().setMessage("Hello World5").call();

        Ref feat1 = git.checkout().setName("feature/feat1").call();
        git.checkout().setName("feature/feat3").call();

        MergeResult mergeResult = git.merge().setStrategy(MergeStrategy.OURS).setMessage("Merge it").include(feat1).call();
        System.out.println(mergeResult.getMergeStatus());

        Ref feat3 = git.checkout().setName("feature/feat3").call();
        git.checkout().setName("feature/feat1").call();
        mergeResult = git.merge().setStrategy(MergeStrategy.OURS).setMessage("Merge it").include(feat3).call();
        System.out.println(mergeResult.getMergeStatus());

//        git.checkout().setName("feature/feat3").call();
//        mergeResult = git.merge().setMessage("Merge it").include(feat1).call();
//        System.out.println(mergeResult.getMergeStatus());
//
//        git.branchCreate().setName("feature/feat4").call();
//        mergeResult = git.merge().setMessage("Merge it 2").include(helloWorld3).call();
//        System.out.println(mergeResult.getMergeStatus());
//
//        System.out.println("hw2 " + helloWorld2.getId());
//        List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
//        branches.forEach(b -> System.out.println(b.getName() + " -> " + b.getObjectId()));
        //        System.out.println("ALL :");
//        List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
//        branches.forEach(System.out::println);

//        File outRepo2 = Files.createTempDirectory("Git").toFile();
//        System.out.println(outRepo2);
//        Git git2 = Git.cloneRepository().setDirectory(outRepo2).setURI("file://" + outRepo.getAbsolutePath()).call();
//
//        System.out.println("ALL :");
//        git2.branchList().setListMode(ListBranchCommand.ListMode.ALL).call().forEach(System.out::println);
//        System.out.println("REMOTE :");
//        git2.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call().forEach(System.out::println);
    }

    private void unzip(String zipFile, String destFolder) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            byte[] buffer = new byte[1024];
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(destFolder + File.separator + entry.getName());
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
            }
        }
    }
}
