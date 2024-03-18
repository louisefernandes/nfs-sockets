package br.edu.ifpb.gugawag.so.sockets;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteNFS {

    public static void main(String[] args) throws IOException {
        System.out.println("== Cliente ==");

        Scanner scanner = new Scanner(System.in);
        Socket socket = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            socket = new Socket("127.0.0.1", 7001);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            while (true) {
                System.out.println("Digite o comando (readdir, rename, create, remove):");
                String comando = scanner.nextLine();
                dos.writeUTF(comando);

                switch (comando) {
                    case "readdir":
                        String listaArquivos = dis.readUTF();
                        System.out.println("Lista de arquivos:\n" + listaArquivos);
                        break;
                    case "rename":
                        System.out.println("Digite o nome do arquivo antigo:");
                        String antigoNome = scanner.nextLine();
                        System.out.println("Digite o novo nome:");
                        String novoNome = scanner.nextLine();
                        dos.writeUTF(antigoNome);
                        dos.writeUTF(novoNome);
                        break;
                    case "create":
                        System.out.println("Digite o nome do novo arquivo:");
                        String novoArquivo = scanner.nextLine();
                        dos.writeUTF(novoArquivo);
                        break;
                    case "remove":
                        System.out.println("Digite o nome do arquivo a ser removido:");
                        String arquivoRemover = scanner.nextLine();
                        dos.writeUTF(arquivoRemover);
                        break;
                    default:
                        System.out.println("Comando inválido.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
