import java.io.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class Motorista {
    private String id;
    private String nome;
    private String cpf;
    private String cnh;
    private Date dataAdmissao;
    private Onibus onibus;

    public Motorista(String id, String nome, String cpf, String cnh, Date dataAdmissao, Onibus onibus) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.cnh = cnh;
        this.dataAdmissao = dataAdmissao;
        this.onibus = onibus;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getCnh() { return cnh; }
    public Date getDataAdmissao() { return dataAdmissao; }
    public Onibus getOnibus() { return onibus; }

    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setCnh(String cnh) { this.cnh = cnh; }
    public void setDataAdmissao(Date dataAdmissao) { this.dataAdmissao = dataAdmissao; }
    public void setOnibus(Onibus onibus) { this.onibus = onibus; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "ID: " + id + ", Nome: " + nome + ", CPF: " + cpf + ", CNH: " + cnh + 
               ", Data Admissão: " + sdf.format(dataAdmissao) + ", Ônibus: " + onibus;
    }

    public static boolean validarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d+")) {
            return false;
        }
        return true;
    }
}

class Onibus {
    private String placa;
    private String modelo;
    private int ano;
    private int capacidade;

    public Onibus(String placa, String modelo, int ano, int capacidade) {
        this.placa = placa;
        this.modelo = modelo;
        this.ano = ano;
        this.capacidade = capacidade;
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public int getAno() { return ano; }
    public int getCapacidade() { return capacidade; }

    public void setPlaca(String placa) { this.placa = placa; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setAno(int ano) { this.ano = ano; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }

    @Override
    public String toString() {
        return "Placa: " + placa + ", Modelo: " + modelo + ", Ano: " + ano + 
               ", Capacidade: " + capacidade + " passageiros";
    }

    public static boolean validarPlaca(String placa) {
        return placa != null && placa.matches("[A-Z]{3}-\\d{4}");
    }
}

class DadoInvalidoException extends Exception {
    public DadoInvalidoException(String message) {
        super(message);
    }
}

public class SistemaMotoristas {
    private static final String ARQUIVO_DADOS = "motoristas.txt";
    private static List<Motorista> motoristas = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        carregarDados();
        exibirMenu();
    }

    private static void exibirMenu() {
        while (true) {
            System.out.println("\n=== Sistema de Motoristas de Ônibus ===");
            System.out.println("1. Criar registro de novo motorista");
            System.out.println("2. Listar todos os registros");
            System.out.println("3. Buscar registro por ID");
            System.out.println("4. Editar registro");
            System.out.println("5. Excluir registro");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        criarMotorista();
                        break;
                    case 2:
                        listarMotoristas();
                        break;
                    case 3:
                        buscarMotoristaPorId();
                        break;
                    case 4:
                        editarMotorista();
                        break;
                    case 5:
                        excluirMotorista();
                        break;
                    case 6:
                        System.out.println("Saindo do sistema...");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
            }
        }
    }

    private static void criarMotorista() {
        try {
            System.out.println("\n--- Cadastro de Novo Motorista ---");

            String id = UUID.randomUUID().toString().substring(0, 8);

            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            if (nome.isEmpty()) throw new DadoInvalidoException("Nome não pode ser vazio!");

            System.out.print("CPF (apenas números): ");
            String cpf = scanner.nextLine();
            if (!Motorista.validarCPF(cpf)) throw new DadoInvalidoException("CPF inválido!");

            System.out.print("CNH: ");
            String cnh = scanner.nextLine();
            if (cnh.isEmpty()) throw new DadoInvalidoException("CNH não pode ser vazia!");

            System.out.print("Data de Admissão (dd/MM/yyyy): ");
            Date dataAdmissao = sdf.parse(scanner.nextLine());

            System.out.println("\n--- Dados do Ônibus ---");
            System.out.print("Placa (formato AAA-9999): ");
            String placa = scanner.nextLine();
            if (!Onibus.validarPlaca(placa)) throw new DadoInvalidoException("Placa inválida!");

            System.out.print("Modelo: ");
            String modelo = scanner.nextLine();
            if (modelo.isEmpty()) throw new DadoInvalidoException("Modelo não pode ser vazio!");

            System.out.print("Ano: ");
            int ano = Integer.parseInt(scanner.nextLine());
            if (ano < 1990 || ano > Calendar.getInstance().get(Calendar.YEAR)) {
                throw new DadoInvalidoException("Ano inválido!");
            }

            System.out.print("Capacidade: ");
            int capacidade = Integer.parseInt(scanner.nextLine());
            if (capacidade <= 0) throw new DadoInvalidoException("Capacidade deve ser positiva!");

            Onibus onibus = new Onibus(placa, modelo, ano, capacidade);
            Motorista motorista = new Motorista(id, nome, cpf, cnh, dataAdmissao, onibus);

            motoristas.add(motorista);
            salvarDados();
            System.out.println("Motorista cadastrado com sucesso! ID: " + id);

        } catch (ParseException e) {
            System.out.println("Formato de data inválido! Use dd/MM/yyyy.");
        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido!");
        } catch (DadoInvalidoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void listarMotoristas() {
        System.out.println("\n--- Lista de Motoristas Cadastrados ---");
        if (motoristas.isEmpty()) {
            System.out.println("Nenhum motorista cadastrado.");
        } else {
            for (Motorista m : motoristas) {
                System.out.println(m);
            }
        }
    }

    private static void buscarMotoristaPorId() {
        System.out.print("\nDigite o ID do motorista: ");
        String id = scanner.nextLine();

        for (Motorista m : motoristas) {
            if (m.getId().equals(id)) {
                System.out.println("\n--- Registro Encontrado ---");
                System.out.println(m);
                return;
            }
        }
        System.out.println("Motorista não encontrado com o ID: " + id);
    }

    private static void editarMotorista() {
        System.out.print("\nDigite o ID do motorista a ser editado: ");
        String id = scanner.nextLine();

        for (Motorista m : motoristas) {
            if (m.getId().equals(id)) {
                try {
                    System.out.println("\n--- Editando Motorista ---");
                    System.out.println("Deixe em branco para manter o valor atual.");

                    System.out.print("Nome (" + m.getNome() + "): ");
                    String nome = scanner.nextLine();
                    if (!nome.isEmpty()) m.setNome(nome);

                    System.out.print("CPF (" + m.getCpf() + "): ");
                    String cpf = scanner.nextLine();
                    if (!cpf.isEmpty()) {
                        if (!Motorista.validarCPF(cpf)) throw new DadoInvalidoException("CPF inválido!");
                        m.setCpf(cpf);
                    }

                    System.out.print("CNH (" + m.getCnh() + "): ");
                    String cnh = scanner.nextLine();
                    if (!cnh.isEmpty()) m.setCnh(cnh);

                    System.out.print("Data de Admissão (" + sdf.format(m.getDataAdmissao()) + "): ");
                    String dataStr = scanner.nextLine();
                    if (!dataStr.isEmpty()) {
                        m.setDataAdmissao(sdf.parse(dataStr));
                    }

                    Onibus o = m.getOnibus();
                    System.out.println("\n--- Editando Ônibus ---");

                    System.out.print("Placa (" + o.getPlaca() + "): ");
                    String placa = scanner.nextLine();
                    if (!placa.isEmpty()) {
                        if (!Onibus.validarPlaca(placa)) throw new DadoInvalidoException("Placa inválida!");
                        o.setPlaca(placa);
                    }

                    System.out.print("Modelo (" + o.getModelo() + "): ");
                    String modelo = scanner.nextLine();
                    if (!modelo.isEmpty()) o.setModelo(modelo);

                    System.out.print("Ano (" + o.getAno() + "): ");
                    String anoStr = scanner.nextLine();
                    if (!anoStr.isEmpty()) {
                        int ano = Integer.parseInt(anoStr);
                        if (ano < 1990 || ano > Calendar.getInstance().get(Calendar.YEAR)) {
                            throw new DadoInvalidoException("Ano inválido!");
                        }
                        o.setAno(ano);
                    }

                    System.out.print("Capacidade (" + o.getCapacidade() + "): ");
                    String capStr = scanner.nextLine();
                    if (!capStr.isEmpty()) {
                        int capacidade = Integer.parseInt(capStr);
                        if (capacidade <= 0) throw new DadoInvalidoException("Capacidade deve ser positiva!");
                        o.setCapacidade(capacidade);
                    }

                    salvarDados();
                    System.out.println("Motorista atualizado com sucesso!");
                    return;

                } catch (ParseException e) {
                    System.out.println("Formato de data inválido! Use dd/MM/yyyy.");
                } catch (NumberFormatException e) {
                    System.out.println("Valor numérico inválido!");
                } catch (DadoInvalidoException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
                return;
            }
        }
        System.out.println("Motorista não encontrado com o ID: " + id);
    }

    private static void excluirMotorista() {
        System.out.print("\nDigite o ID do motorista a ser excluído: ");
        String id = scanner.nextLine();

        Iterator<Motorista> iterator = motoristas.iterator();
        while (iterator.hasNext()) {
            Motorista m = iterator.next();
            if (m.getId().equals(id)) {
                iterator.remove();
                salvarDados();
                System.out.println("Motorista removido com sucesso!");
                return;
            }
        }
        System.out.println("Motorista não encontrado com o ID: " + id);
    }

    private static void carregarDados() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_DADOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 9) {
                    String id = dados[0];
                    String nome = dados[1];
                    String cpf = dados[2];
                    String cnh = dados[3];
                    Date dataAdmissao = sdf.parse(dados[4]);

                    String placa = dados[5];
                    String modelo = dados[6];
                    int ano = Integer.parseInt(dados[7]);
                    int capacidade = Integer.parseInt(dados[8]);

                    Onibus onibus = new Onibus(placa, modelo, ano, capacidade);
                    Motorista motorista = new Motorista(id, nome, cpf, cnh, dataAdmissao, onibus);
                    motoristas.add(motorista);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de dados não encontrado. Criando novo...");
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private static void salvarDados() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_DADOS))) {
            for (Motorista m : motoristas) {
                Onibus o = m.getOnibus();
                pw.println(String.join(";", 
                    m.getId(),
                    m.getNome(),
                    m.getCpf(),
                    m.getCnh(),
                    sdf.format(m.getDataAdmissao()),
                    o.getPlaca(),
                    o.getModelo(),
                    String.valueOf(o.getAno()),
                    String.valueOf(o.getCapacidade())
                ));
            }
        } catch (Exception e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }
}