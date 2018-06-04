package main.context;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class ContextHolder {

    private ContextHolder() {}

    private static Map<String, VariableData> symbolsTable = new HashMap<>();

    private static Map<String, String> localSymbolsMapper = new HashMap<>();

    private static List<Map<String, String>> symbolsMappersList = new ArrayList<>();

    private static Set<String> actualContextVariables = new HashSet<>();

    private static List<Set<String>> contextVariablesList = new ArrayList<>();

    private static Boolean isGlobalContext = true;

    private static Integer numOfIndents = 0;

    private static PrintWriter fileWriter = new PrintWriter(System.out, true);

    public static void printBeginning() {
        fileWriter.println("#include <iostream>");
        fileWriter.println("#include <armadillo>");
        fileWriter.println("#include <vector>");
        fileWriter.println();
        fileWriter.println("constexpr unsigned int str2int(const char* str, int h = 0)");
        fileWriter.println("{");
        fileWriter.println("\treturn !str[h] ? 5381 : (str2int(str, h + 1) * 33) ^ str[h];");
        fileWriter.println("}");
        fileWriter.println();
        fileWriter.println("int main()");
        fileWriter.println("{");
        fileWriter.flush();
        ++numOfIndents;
    }

    public static void printEnding() {
        fileWriter.println("\treturn 0;");
        fileWriter.println("}");
        fileWriter.flush();
    }

    public static void setWriter(String path) {
        try {
            fileWriter = new PrintWriter(path);
            System.out.println("Printing to " + path);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Cannot create file in a given place! Writing to stdout.");
            fileWriter = new PrintWriter(System.out);
        }
    }

    public static Map<String, VariableData> getSymbolsTable() {
        return symbolsTable;
    }

    public static void setSymbolsTable(Map<String, VariableData> symbolsTable) {
        ContextHolder.symbolsTable = symbolsTable;
    }

    public static Map<String, String> getLocalSymbolsMapper() {
        return localSymbolsMapper;
    }

    public static void setLocalSymbolsMapper(Map<String, String> localSymbolsMapper) {
        ContextHolder.localSymbolsMapper = localSymbolsMapper;
    }

    public static StringBuilder addIndents() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numOfIndents; ++i)
            sb.append("\t");
        return sb;
    }

    public static Boolean getIsGlobalContext() {
        return isGlobalContext;
    }

    public static void setIsGlobalContext(Boolean isGlobalContext) {
        ContextHolder.isGlobalContext = isGlobalContext;
    }

    public static Integer getNumOfIndents() {
        return numOfIndents;
    }

    public static void setNumOfIndents(Integer numOfIndents) {
        ContextHolder.numOfIndents = numOfIndents;
    }

    public static PrintWriter getFileWriter() {
        return fileWriter;
    }

    public static void setFileWriter(PrintWriter fileWriter) {
        ContextHolder.fileWriter = fileWriter;
    }

    public static Set<String> getActualContextVariables() {
        return actualContextVariables;
    }

    public static List<Set<String>> getContextVariablesList() {
        return contextVariablesList;
    }

    public static void setActualContextVariables(Set<String> actualContextVariables) {
        ContextHolder.actualContextVariables = actualContextVariables;
    }

    public static void setContextVariablesList(List<Set<String>> contextVariablesList) {
        ContextHolder.contextVariablesList = contextVariablesList;
    }

    public static List<Map<String, String>> getSymbolsMappersList() {
        return symbolsMappersList;
    }

    public static void setSymbolsMappersList(List<Map<String, String>> symbolsMappersList) {
        ContextHolder.symbolsMappersList = symbolsMappersList;
    }

    public static void changeContext() {
        ++numOfIndents;
        isGlobalContext = false;
        contextVariablesList.add(actualContextVariables);
        actualContextVariables = new HashSet<>();
        symbolsMappersList.add(localSymbolsMapper);
        localSymbolsMapper = new HashMap<>();
    }

    public static void restoreContext() {
        --numOfIndents;
        if (getNumOfIndents() == 1)
            isGlobalContext = true;
        actualContextVariables = contextVariablesList.remove(contextVariablesList.size() - 1);
        localSymbolsMapper = symbolsMappersList.remove(symbolsMappersList.size() - 1);
    }
}
