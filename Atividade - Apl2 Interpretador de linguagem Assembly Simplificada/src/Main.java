//Matheus Veiga Bacetic Joaquim | RA: 10425638
//João Vitor Rocha Miranda | RA: 10427273
//Gabriel Pereira Faravola | RA: 10427189

//Bibliografia:  https://chatgpt.com/share/673e0d3b-59ac-8002-b5c4-b469f4527819

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AssemblyInterpreter interpreter = new AssemblyInterpreter();
        CommandHandler handler = new CommandHandler(interpreter);

        Scanner consoleInput = new Scanner(System.in);
        String command;

        System.out.println("Bem-vindo ao Assembly Interpreter!");
        System.out.println("Comandos disponíveis: load, list, run, ins, del, save, exit.");

        while (true) {
            System.out.print("> ");
            command = consoleInput.nextLine();
            String[] commandParts = command.split(" ");

            switch (commandParts[0].toLowerCase()) {
                case "load":
                    if (commandParts.length >= 2) {
                        String filePath = command.substring(command.indexOf(" ") + 1).trim();
                        handler.load(filePath);
                    } else {
                        System.out.println("Uso: load <endereço do arquivo>.");
                    }
                    break;
                case "list":
                    handler.list();
                    break;
                case "run":
                    handler.run();
                    break;
                case "ins":
                    try {
                        String input = command.substring(4).trim();
                        int spaceIndex = input.indexOf(" ");
                        if (spaceIndex == -1) {
                            System.out.println("Erro: Formato inválido.\nUso: ins <linha> <instrução>.");
                            break;
                        }

                        int line = Integer.parseInt(input.substring(0, spaceIndex).trim());
                        String instruction = input.substring(spaceIndex + 1).trim();

                        if (line < 1 || line > 100) {
                            System.out.println("Erro: O número da linha deve estar entre 1 e 100.");
                            break;
                        }

                        handler.insert(line, instruction);
                    } catch (NumberFormatException e) {
                        System.out.println("Erro: Número da linha inválido.\nUso: ins <linha> <instrução>.");
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.println("Erro: Nenhuma instrução fornecida.\nUso: ins <linha> <instrução>.");
                    }
                    break;
                case "del":
                    if (commandParts.length == 2) {
                    	if(interpreter.isNumeric(commandParts[1])) {
                    		int line = Integer.parseInt(commandParts[1]);
                            handler.delete(String.valueOf(line));
                    	} else {
                    		System.out.println("Uso: <linha> deve ser um número inteiro.");
                        	break;
                    	}
                    	
                    } else if (commandParts.length == 3) {
                        if(interpreter.isNumeric(commandParts[1]) && interpreter.isNumeric(commandParts[2])) {
                        	int startLine = Integer.parseInt(commandParts[1]);
                            int endLine = Integer.parseInt(commandParts[2]);
                            handler.delete(startLine, endLine);
                        } else {
                        	System.out.println("Uso: <linha_inicial> <linha_final> não são números inteiros.");
                        	break;
                        }
                    	
                    } else {
                        System.out.println("Uso: del <linha> ou del <linha_inicial> <linha_final>.");
                    }
                    break;
                case "save":
                    if (commandParts.length == 2) {
                        handler.save(commandParts[1]);
                    } else {
                        handler.save();
                    }
                    break;
                case "exit":
                    handler.exit();
                    return;
                default:
                    System.out.println("Comando não reconhecido.");
            }
        }
    }
}
