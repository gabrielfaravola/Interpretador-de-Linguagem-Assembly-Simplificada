//Matheus Veiga Bacetic Joaquim | RA: 10425638
//João Vitor Rocha Miranda | RA: 10427273
//Gabriel Pereira Faravola | RA: 10427189

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CommandHandler {
    private AssemblyInterpreter interpreter;
    private MyLinkedList<String> instructions;
    private String loadedFileName;
    private boolean isModified;

    public CommandHandler(AssemblyInterpreter interpreter) {
        this.interpreter = interpreter;
        this.instructions = new MyLinkedList<>();
        this.isModified = false;
    }

    public void load(String filePath) {
        if (isModified) {
            System.out.print("Existem modificações não salvas. Deseja salvar antes de carregar outro arquivo? (S/N): ");
            Scanner input = new Scanner(System.in);
            String response = input.nextLine().toUpperCase();
            if (response.equals("S")) {
                save();
                isModified = false;
            }
        }

        MyLinkedList<String> loadedInstructions = interpreter.loadInstructions(filePath);

        if (loadedInstructions == null || loadedInstructions.isEmpty()) {
            System.out.println("Erro ao carregar o arquivo. Verifique o caminho completo e tente novamente.");
        } else {
            this.instructions = loadedInstructions;
            this.loadedFileName = filePath;
            this.isModified = false;
            System.out.println("Arquivo carregado com sucesso.");
        }
    }

    public void list() {
        if (instructions == null || instructions.isEmpty()) {
            System.out.println("Nenhum código na memória.");
            return;
        }

        int count = 0; // Contador para linhas exibidas
        for (int i = 0; i < instructions.size(); i++) {
            String instruction = instructions.get(i);
            if (instruction != null && !instruction.trim().isEmpty()) { // Verifica se a instrução não está vazia
                System.out.println((i + 1) + " " + instruction);
                count++;

                // Pausa após 20 linhas
                if (count % 20 == 0) {
                    System.out.println("\n--- Pressione Enter para continuar ---");
                    try {
                        System.in.read(); // Aguarda entrada do usuário
                    } catch (IOException e) {
                        System.err.println("Erro ao aguardar entrada do usuário.");
                    }
                }
            }
        }
    }

    public void run() {
        if (instructions == null || instructions.isEmpty()) {
            System.out.println("Nenhum código na memória.");
            return;
        }

        try {
            for (int i = 0; i < instructions.size(); i++) {
                String instruction = instructions.get(i);
                if (instruction == null || instruction.trim().isEmpty()) {
                    continue;
                }
            }

            interpreter.execute(instructions);
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && !errorMessage.isEmpty()) {
                try {
                    String[] messageParts = errorMessage.split(": ");
                    String instruction = messageParts[0].trim();
                    String errorDetail = messageParts.length > 1 ? messageParts[1].trim() : "Erro desconhecido.";

                    int lineNumber = instructions.indexOf(instruction);

                    if (lineNumber != -1) {
                        System.out.println("Erro na linha " + (lineNumber + 1) + ": " + errorDetail);
                    } else {
                        System.out.println("Erro: Instrução não encontrada na lista.");
                    }
                } catch (Exception ex) {
                    System.out.println("Erro ao processar a mensagem de erro: " + ex.getMessage());
                }
            } else {
                System.out.println("Erro desconhecido: " + errorMessage);
            }
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }



    public void insert(int line, String instruction) {
        if (instructions == null || instructions.isEmpty()) {
            System.out.println("Nenhum código na memória. Não é possível inserir nova linha.");
            return;
        }

        if (line < 1 || line > 100) {
            System.out.println("Número da linha deve estar entre 1 e 100.");
            return;
        }

        int index = line - 1;

        if (index < instructions.size()) {
            instructions.set(index, instruction);
            if(instructions != null){
                System.out.println(String.format("Linha atualizada:\n%d %s", line, instruction));
            }
        } else {
            while (instructions.size() < index) {
                instructions.add(" ");
            }
            instructions.add(instruction);
            System.out.println(String.format("Linha inserida:\n%d %s", line, instruction));
        }

        isModified = true;
    }




    public void delete(String line) {
        line = line.replace("\0", "").trim();

        if (line.isEmpty()) {
            System.out.println("Erro: Número da linha inválido. Por favor, forneça um número de linha.");
            return;
        }

        try {
            int lineNumber = Integer.parseInt(line);
            int index = lineNumber - 1;

            if (instructions == null || instructions.isEmpty()) {
                System.out.println("Nenhum código na memória.");
                return;
            }

            if (index < 0 || index >= instructions.size() || instructions.get(index) == null) {
                System.out.println("Erro: Linha "+ line +" inexistente.");
                return;
            }
            System.out.println("Linha removida: ");
            instructions.remove(index);

            isModified = true;
        } catch (NumberFormatException e) {
            System.out.println("Erro: Número da linha inválido. Certifique-se de fornecer um número inteiro.");
        }
    }

    public void delete(int startLine, int endLine) {
        int startIndex = startLine - 1;
        int endIndex = endLine - 1;

        if (instructions == null || instructions.isEmpty()) {
            System.out.println("Nenhum código na memória.");
            return;
        }
        if (startIndex < 0 || endIndex >= instructions.size() || startIndex > endIndex ||
                instructions.get(startIndex) == null || instructions.get(endIndex) == null) {
            System.out.println("Intervalo de linhas inválido.");
            return;
        }
        System.out.println("Linhas removidas: ");
        for (int i = startIndex; i <= endIndex; i++) {
            instructions.remove(i);
        }

        isModified = true;
    }

    public void save() {
    	if (instructions == null || instructions.isEmpty()) {
            System.out.println("Nenhum código na memória.");
            return;
        }
    	if (loadedFileName == null) {
            System.out.println("Nenhum arquivo carregado. Use SAVE <ARQUIVO.ED1> para salvar em um novo arquivo.");
            return;
        }
        save(loadedFileName);
    }

    public void save(String fileName) {
        if (instructions == null || instructions.isEmpty()) {
            System.out.println("Nenhum código na memória.");
            return;
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 0; i < instructions.size(); i++) {
                String instruction = instructions.get(i);

                if (instruction == null || instruction.trim().isEmpty()) {
                    continue;
                }

                writer.write((i + 1) * 10 + " " + instruction + "\n");
            }
            isModified = false;
            System.out.println("Arquivo salvo com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo.");
        }
    }


    public void exit() {
        if (isModified) {
            System.out.print("Existem modificações não salvas. Deseja salvar antes de sair? (S/N): ");
            Scanner input = new Scanner(System.in);
            String response = input.nextLine().toUpperCase();
            if (response.equals("S")) {
                save();
                isModified = false;
                
            }
        }
        System.out.println("Programa encerrado.");
    }
}