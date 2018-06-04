package main.context;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class ContextHolder {

    private ContextHolder() {}

    private static Map<String, VariableData> symbolsTable = new HashMap<>();

    private static Map<String, String> localSymbolsMapper = new HashMap<>();

    private static List<Map<String, String>> symbolsMappersList = new ArrayList<>();

    private static List<String> actualContextVariables = new ArrayList<>();

    private static List<List<String>> contextVariablesList = new ArrayList<>();

    private static Integer numOfIndents = 0;

    private static PrintWriter fileWriter = new PrintWriter(System.out, true);

    private static StringBuilder varsToPrint = new StringBuilder();

    public static void printBeginning() {
        fileWriter.println("#include <iostream>");
        fileWriter.println("#include <armadillo>");
        fileWriter.println("#include <vector>");
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

    public static List<String> getActualContextVariables() {
        return actualContextVariables;
    }

    public static List<List<String>> getContextVariablesList() {
        return contextVariablesList;
    }


    public static List<Map<String, String>> getSymbolsMappersList() {
        return symbolsMappersList;
    }


    public static void changeContext() {
        ++numOfIndents;
        contextVariablesList.add(actualContextVariables);
        actualContextVariables = new ArrayList<>();
        symbolsMappersList.add(localSymbolsMapper);
        localSymbolsMapper = new HashMap<>();
    }

    public static void restoreContext() {
        --numOfIndents;
        List<String> previousSet = actualContextVariables;
        Map<String, String> previousMap = localSymbolsMapper;
        actualContextVariables = contextVariablesList.remove(contextVariablesList.size() - 1);
        localSymbolsMapper = symbolsMappersList.remove(symbolsMappersList.size() - 1);
        varsToPrint = new StringBuilder();
        for (String var : previousSet) {
            String id = var;
            while (previousMap.get(id) != null)
                id = previousMap.get(id);
            VariableData varData = symbolsTable.get(id);
            if (!actualContextVariables.contains(id)) {
                varsToPrint.append(addIndents().toString()).append("auto ").append(id).append(" = ").append(varData.getValue()).append(";\n");
                actualContextVariables.add(id);
                if (!var.equals(id))
                    localSymbolsMapper.put(var, id);
            }
        }
        if (varsToPrint.length() != 0)
            varsToPrint.deleteCharAt(varsToPrint.length() - 1);
    }

    public static StringBuilder getVarsToPrint() {
        return varsToPrint;
    }
}
