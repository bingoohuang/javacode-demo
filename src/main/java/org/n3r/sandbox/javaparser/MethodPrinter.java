package org.n3r.sandbox.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;

public class MethodPrinter {
    /**
     * main method.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        CompilationUnit cu;
        try (FileInputStream in = new FileInputStream("src/main/java/org/n3r/sandbox/javaparser/MethodPrinter.java")) {
            // parse the file
            cu = JavaParser.parse(in);
        }

        // visit and print the methods names
        new MethodVisitor().visit(cu, null);
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodVisitor extends VoidVisitorAdapter {

        // visit method
        // another

        /**
         * xxx
         * @param n
         * @param arg
         */
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this
            // CompilationUnit, including inner class methods
            System.out.println(n.getName());
            System.out.println(n.getComment());
        }
    }
}
