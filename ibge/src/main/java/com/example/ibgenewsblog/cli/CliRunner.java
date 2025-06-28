package com.example.ibgenewsblog.cli;

import com.example.ibgenewsblog.model.Noticia;
import com.example.ibgenewsblog.model.Usuario;
import com.example.ibgenewsblog.service.IbgeNewsService;
import com.example.ibgenewsblog.service.PersistenceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class CliRunner implements CommandLineRunner {

    private final IbgeNewsService ibgeNewsService;
    private final PersistenceService persistenceService;
    private Usuario currentUser;
    private final Scanner scanner = new Scanner(System.in);

    public CliRunner(IbgeNewsService ibgeNewsService, PersistenceService persistenceService) {
        this.ibgeNewsService = ibgeNewsService;
        this.persistenceService = persistenceService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Bem-vindo ao Blog de Noticias do IBGE!");

        currentUser = persistenceService.loadUser();
        if (currentUser == null) {
            System.out.print("Por favor, digite seu nome ou apelido: ");
            String userName = scanner.nextLine();
            currentUser = new Usuario();
            currentUser.setNome(userName);
            System.out.println("Ola, " + userName + "!");
            setupInitialNews();
        } else {
            System.out.println("Ola novamente, " + currentUser.getNome() + "!");
        }

        while (true) {
            printMenu();
            int choice = getIntInput();

            try {
                switch (choice) {
                    case 1:
                        searchNews();
                        break;
                    case 2:
                        addNoticiaToList(currentUser.getFavoritos(), "favoritos");
                        break;
                    case 3:
                        addNoticiaToList(currentUser.getLidas(), "lidas");
                        break;
                    case 4:
                        addNoticiaToList(currentUser.getParaLerDepois(), "para ler depois");
                        break;
                    case 5:
                        removeNoticiaFromList(currentUser.getFavoritos(), "favoritos");
                        break;
                    case 6:
                        removeNoticiaFromList(currentUser.getLidas(), "lidas");
                        break;
                    case 7:
                        removeNoticiaFromList(currentUser.getParaLerDepois(), "para ler depois");
                        break;
                    case 8:
                        showLists();
                        break;
                    case 9:
                        persistenceService.saveUser(currentUser);
                        System.out.println("Dados salvos. Ate mais!");
                        return;
                    default:
                        System.out.println("Opcao invalida.");
                }
            } catch (Exception e) {
                System.err.println("Ocorreu um erro: " + e.getMessage());
                // e.printStackTrace(); DD
            }
        }
    }

    private void setupInitialNews() {
        System.out.println("Carregando algumas noticias recentes para voce comecar...");
        List<Noticia> recentNews = ibgeNewsService.buscarNoticiasRecentes();
        if (!recentNews.isEmpty()) {
            currentUser.getParaLerDepois().addAll(recentNews.stream().limit(5).collect(Collectors.toList()));
            System.out.println("5 noticias recentes foram adicionadas a sua lista 'para ler depois'.");
        } else {
            System.out.println("Nao foi possivel carregar noticias recentes.");
        }
    }

    private void printMenu() {
        System.out.println("\nO que voce gostaria de fazer?");
        System.out.println("1. Procurar noticias");
        System.out.println("2. Adicionar noticia aos favoritos");
        System.out.println("3. Marcar noticia como lida");
        System.out.println("4. Adicionar noticia para ler depois");
        System.out.println("5. Remover noticia dos favoritos");
        System.out.println("6. Remover noticia das lidas");
        System.out.println("7. Remover noticia das para ler depois");
        System.out.println("8. Exibir e ordenar listas");
        System.out.println("9. Salvar e sair");
        System.out.print("Escolha uma opcao: ");
    }

    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada invalida. Por favor, digite um numero.");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    private void searchNews() {
        System.out.println("Como voce gostaria de procurar noticias?");
        System.out.println("1. Por titulo/palavra-chave");
        System.out.println("2. Por tipo/categoria");
        System.out.print("Escolha uma opcao: ");
        int searchChoice = getIntInput();

        List<Noticia> resultados = new ArrayList<>();
        if (searchChoice == 1) {
            System.out.print("Digite o titulo ou palavra-chave: ");
            String query = scanner.nextLine();
            resultados = ibgeNewsService.buscarNoticias(query);
        } else if (searchChoice == 2) {
            System.out.print("Digite o tipo/categoria (ex: 'noticia', 'release'): ");
            String tipo = scanner.nextLine();
            resultados = ibgeNewsService.buscarNoticiasPorTipo(tipo);
        } else {
            System.out.println("Opcao de busca invalida.");
            return;
        }

        if (resultados.isEmpty()) {
            System.out.println("Nenhuma noticia encontrada.");
        } else {
            System.out.println("Resultados da busca:");
            printNoticiaList(resultados);
            System.out.print("Digite o numero da noticia para ver detalhes (ou 0 para voltar): ");
            int detailChoice = getIntInput();
            if (detailChoice > 0 && detailChoice <= resultados.size()) {
                System.out.println(resultados.get(detailChoice - 1));
            }
        }
    }

    private void addNoticiaToList(List<Noticia> list, String listName) {
        System.out.print("Digite o titulo ou palavra-chave da noticia para adicionar a '" + listName + "': ");
        String query = scanner.nextLine();
        List<Noticia> resultados = ibgeNewsService.buscarNoticias(query);

        if (resultados.isEmpty()) {
            System.out.println("Nenhuma noticia encontrada.");
            return;
        }

        System.out.println("Selecione a noticia para adicionar:");
        printNoticiaList(resultados);
        int choice = getIntInput();

        if (choice > 0 && choice <= resultados.size()) {
            Noticia selectedNoticia = resultados.get(choice - 1);
            if (!list.contains(selectedNoticia)) {
                list.add(selectedNoticia);
                System.out.println("'" + selectedNoticia.getTitulo() + "' adicionada a " + listName + ".");
            } else {
                System.out.println("Essa noticia ja esta na lista.");
            }
        } else {
            System.out.println("Selecao invalida.");
        }
    }

    private void removeNoticiaFromList(List<Noticia> list, String listName) {
        if (list.isEmpty()) {
            System.out.println("A lista '" + listName + "' esta vazia.");
            return;
        }

        System.out.println("Selecione a noticia para remover de '" + listName + "':");
        printNoticiaList(list);
        int choice = getIntInput();

        if (choice > 0 && choice <= list.size()) {
            Noticia removedNoticia = list.remove(choice - 1);
            System.out.println("'" + removedNoticia.getTitulo() + "' removida de " + listName + ".");
        } else {
            System.out.println("Selecao invalida.");
        }
    }

    private void showLists() {
        System.out.println("Qual lista voce gostaria de exibir?");
        System.out.println("1. Favoritos");
        System.out.println("2. Lidas");
        System.out.println("3. Para ler depois");
        System.out.print("Escolha uma opcao: ");
        int choice = getIntInput();

        List<Noticia> listToShow = null;
        String listName = "";
        switch (choice) {
            case 1: listToShow = currentUser.getFavoritos(); listName = "Favoritos"; break;
            case 2: listToShow = currentUser.getLidas(); listName = "Lidas"; break;
            case 3: listToShow = currentUser.getParaLerDepois(); listName = "Para ler depois"; break;
            default: System.out.println("Opcao invalida."); return;
        }

        if (listToShow.isEmpty()) {
            System.out.println("A lista '" + listName + "' esta vazia.");
            return;
        }

        System.out.println("Como voce gostaria de ordenar a lista?");
        System.out.println("1. Ordem alfabetica do titulo (A-Z)");
        System.out.println("2. Data de publicacao (mais recente primeiro)");
        System.out.println("3. Tipo/Categoria");
        System.out.println("0. Sem ordenacao");
        System.out.print("Escolha uma opcao: ");
        int sortChoice = getIntInput();

        switch (sortChoice) {
            case 1: listToShow.sort(Comparator.comparing(Noticia::getTitulo)); break;
            case 2: listToShow.sort(Comparator.comparing(Noticia::getDataPublicacao, Comparator.nullsLast(String::compareTo)).reversed()); break;
            case 3: listToShow.sort(Comparator.comparing(Noticia::getTipo, Comparator.nullsLast(String::compareTo))); break;
            case 0: break;
            default: System.out.println("Opcao de ordenacao invalida. Exibindo na ordem padrao.");
        }

        System.out.println("\n--- " + listName + " ---");
        for (Noticia noticia : listToShow) {
            System.out.println(noticia);
        }
    }

    private void printNoticiaList(List<Noticia> noticias) {
        for (int i = 0; i < noticias.size(); i++) {
            System.out.println((i + 1) + ". " + noticias.get(i).getTitulo());
        }
    }
}