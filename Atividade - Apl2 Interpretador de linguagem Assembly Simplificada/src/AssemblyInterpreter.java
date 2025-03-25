//Gabriel Pereira Faravola

import java.util.Scanner;

public class AssemblyInterpreter {

    // Registradores de A a Z
    private final MyHashMap<Character, Integer> registers = new MyHashMap<>();

    public AssemblyInterpreter() {
        // Inicializa os registradores (A-Z) com valor 0
        for (char c = 'A'; c <= 'Z'; c++) {
            registers.put(c, 0);
        }
    }
    public boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;         
        } catch (NumberFormatException e) {
            return false;          
        }
    }
    public void execute(MyLinkedList<String> instructions) {
        int i = 0;
        while (i < instructions.size()) {
            String currentInstruction = instructions.get(i);
            if (currentInstruction == null || currentInstruction.trim().isEmpty()) {
                i++;
                continue;
            }

            try {
                String[] parts = currentInstruction.split(" ");
                if (parts.length == 0 || parts[0] == null || parts[0].trim().isEmpty()) {
                    i++;
                    continue;
                }

                String command = parts[0];
                char x = parts.length > 1 ? parts[1].charAt(0) : ' ';
                int valueX = registers.getOrDefault(x, 0);

                switch (command) {
                    case "mov":
                    	if (parts.length < 3) {
                        	System.out.print("Erro: Comando 'mov' incompleto.\nLinha: "+ (i+1) +" "+parts[0]);
                        	if(parts.length == 2) {
                        		System.out.println(" "+parts[1]);
                        	}else {
                        		System.out.println();
                        	}
                            return;
                        }
                        try {
                            int valueY = Integer.parseInt(parts[2]);
                            registers.put(x, valueY);
                        } catch (NumberFormatException e) {
                            char y = parts[2].charAt(0);
                            registers.put(x, registers.getOrDefault(y, 0));
                        }
                        break;
                    case "inc":
                        registers.put(x, valueX + 1);
                        break;
                    case "dec":
                        registers.put(x, valueX - 1);
                        break;
                    case "add":
                        if (parts.length < 3) {
                        	System.out.print("Erro: Comando 'add' incompleto.\nLinha: "+ (i+1) +" "+parts[0]);
                        	if(parts.length == 2) {
                        		System.out.println(" "+parts[1]);
                        	}else {
                        		System.out.println();
                        	}
                            return;
                        }
                        String yValue = parts[2];

                        if (isNumeric(yValue)) {
                            int yInt = Integer.parseInt(yValue);
                            registers.put(x, valueX + yInt);
                        } else {
                            char yChar = yValue.charAt(0);
                            registers.put(x, valueX + registers.getOrDefault(yChar, 0));
                        }
                        break;
                        
                    case "sub":
                    	if (parts.length < 3) {
                        	System.out.print("Erro: Comando 'sub' incompleto.\nLinha: "+ (i+1) +" "+parts[0]);
                        	if(parts.length == 2) {
                        		System.out.println(" "+parts[1]);
                        	}else {
                        		System.out.println();
                        	}
                            return;
                        }
                        try {
                            int valueY = Integer.parseInt(parts[2]);
                            registers.put(x, valueX - valueY);
                        } catch (NumberFormatException e) {
                            char y = parts[2].charAt(0);
                            registers.put(x, valueX - registers.getOrDefault(y, 0));
                        }
                        break;
                    case "mul":
                    	if (parts.length < 3) {
                        	System.out.print("Erro: Comando 'mul' incompleto.\nLinha: "+ (i+1) +" "+parts[0]);
                        	if(parts.length == 2) {
                        		System.out.println(" "+parts[1]);
                        	}else {
                        		System.out.println();
                        	}
                            return;
                        }
                        try {
                            int valueY = Integer.parseInt(parts[2]);
                            registers.put(x, valueX * valueY);
                        } catch (NumberFormatException e) {
                            char y = parts[2].charAt(0);
                            registers.put(x, valueX * registers.getOrDefault(y, 0));
                        }
                        break;
                    case "div":
                    	if (parts.length < 3) {
                        	System.out.print("Erro: Comando 'div' incompleto.\nLinha: "+ (i+1) +" "+parts[0]);
                        	if(parts.length == 2) {
                        		System.out.println(" "+parts[1]);
                        	}else {
                        		System.out.println();
                        	}
                            return;
                        }
                    
                        try {
                            int valueY = Integer.parseInt(parts[2]);
                            if (valueY != 0) {
                                registers.put(x, valueX / valueY);
                            }else {
                            	System.out.println("Erro: Comando 'div' realizando divisão por '0'.\nLinha: "+ (i+1)+" "+parts[0]+" "+parts[1]+" "+parts[2]);
                            	return;
                            }
                        } catch (NumberFormatException e) {
                            char y = parts[2].charAt(0);
                            if (registers.getOrDefault(y, 0) != 0) {
                                registers.put(x, valueX / registers.getOrDefault(y, 0));
                            } else {
                            	System.out.println("Erro: Comando 'div' realizando divisão por '0'.\nLinha: "+ (i+1)+" "+parts[0]+" "+parts[1]+" "+parts[2]);
                            	return;
                            }
                        }
                        break;
                    case "jnz":
                    	if (parts.length < 3) {
                        	System.out.print("Erro: Comando 'jnz' incompleto.\nLinha: "+ (i+1) +" "+parts[0]);
                        	if(parts.length == 2) {
                        		System.out.println(" "+parts[1]);
                        	}else {
                        		System.out.println();
                        	}
                            return;
                    	}
                        try {
                            int line = Integer.parseInt(parts[2]);
                            if (valueX != 0) {
                                int newIndex = (line / 10) - 1;
                                if (newIndex >= 0 && newIndex < instructions.size()) {
                                    i = newIndex;
                                    continue;
                                }
                            }
                        } catch (NumberFormatException e) {
                         
                        }
                        break;
                    case "out":
                        if (parts.length < 2) {
                            System.out.println("Erro: Comando 'out' requer um registrador.");
                        } else {
                            char register = parts[1].charAt(0);
                            if (!registers.containsKey(register)) {
                                System.out.println("Erro: O registrador '" + register + "' não foi definido.");
                                return;
                            } else {
                                System.out.println(valueX);
                            }
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                System.out.println("Erro inesperado na linha " + (i + 1) + ": " + e.getMessage()+".");
            }
            i++;
        }
    }

    public MyLinkedList<String> loadInstructions(String filePath) {
        MyLinkedList<String> instructions = new MyLinkedList<>();
        try (Scanner scanner = new Scanner(new java.io.File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String instruction = line.substring(line.indexOf(" ") + 1);
                    instructions.add(instruction);
                }
            }
            System.out.println("Instruções carregadas do arquivo: " + filePath+".");
        } catch (java.io.FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + filePath+".");
        }
        return instructions;
    }
}
