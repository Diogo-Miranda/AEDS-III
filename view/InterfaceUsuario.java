class InterfaceUsuario {

    public static boolean validarEntrada(String entrada) {
        boolean error = false;
        
        try {   
            int number = Integer.parseInt(entrada);
            error = number < 0 || number > 5; 
        } catch (NumberFormatException nfe) {
            error = true;
        }

        return error;
    }

    public static String input() throws IOException {
        InputStream is = System.in;
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String entrada = br.readLine();

        return entrada;
    }

    public static void imprimirMenu() { 
        System.out.println("\n====== Operações para CRUD ======");
        System.out.println("(1) Criar um comentário");
        System.out.println("(2) Atualizar comentário");
        System.out.println("(3) Pesquisar comentário");
        System.out.println("(4) Deletar comentário");
        System.out.println("(0) Sair");
    }

    public static void create() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
        System.out.println("\nInforme o nome do autor: ");
        String nomeAutor = input();
        System.out.println("\nInforme o título: ");
        String titulo = input();
        System.out.println("\nInforme o corpo do comentário: ");
        String corpo = input();
        
        Comentario comentario = new Comentario(-1, nomeAutor, titulo, corpo);

        int id = arqComentario.create(comentario);

        System.out.println("\nComentário criado - ID["+id+"]");
    }   

    public static void update() throws Exception, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        System.out.println("\nEscolha o ID do comentário a ser atualizado: ");
        String entradaID = input();
        boolean error = false;

        try {
            int id = Integer.parseInt(entradaID);
            Comentario comentario = arqComentario.read(id);
            if(comentario != null) {
                System.out.println("\nInforme o nome do autor: ");
                String nomeAutor = input();
                comentario.setAuthorName(nomeAutor);

                System.out.println("\nInforme o título: ");
                String titulo = input();
                comentario.setTitle(titulo);

                System.out.println("\nInforme o corpo do comentário: ");
                String corpo = input();
                comentario.setBody(corpo);

                comentario.setDate(new Date());

                error = !arqComentario.update(comentario);
            } else {
                error = true;
            }
        } catch (NumberFormatException nfe) {
            error = true;
        }
            
        if(error) {
            System.out.println("\nOcorreu algum erro ao atualizar este comentário");
        } else {
            System.out.println("\nComentário atualizado com sucesso!!");
        }
    }

    public static void search() throws Exception, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        System.out.println("\nInforme o ID a ser pesquisado: ");
        String entradaId = input();

        boolean error = false;
        try {
            int id = Integer.parseInt(entradaId);
            Comentario comentario = arqComentario.read(id);
            if(comentario != null) {
                System.out.println(comentario.toString());
            } else {
                error = true;
            }
        } catch (NumberFormatException nfe) {
            error = true;
        }

        if(error) {
            System.out.println("\nOcorreu algum erro na procura do comentário");
        } 
    }

    public static void delete() throws Exception, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        System.out.println("\nInforme o ID a ser deletado: ");
        String entradaId = input();

        boolean error = false;
        try {
            int id = Integer.parseInt(entradaId);
            error = !arqComentario.delete(id);
        } catch (NumberFormatException nfe) {
            error = true;
        }

        if(error) {
            System.out.println("\nOcorreu algum erro na exclusão do comentário");
        } else {
            System.out.println("\nComentário excluído com sucesso!");
        }
    }

    public static void escolherMetodo(int opcao) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
        switch(opcao) {
            case 1:
                create();
                break;
            case 2:
                update();
                break;
            case 3:
                search();
                break;
            case 4:
                delete();
                break;
            case 0:
                System.out.println("\nObrigado por utilizar meu programa. By: Diogo");
                break;
            default:
                break;
        }
    }

}