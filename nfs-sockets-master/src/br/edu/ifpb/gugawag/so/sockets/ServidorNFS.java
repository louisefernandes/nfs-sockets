package br.edu.ifpb.gugawag.so.sockets;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServidorNFS {

    private static List<String> arquivos = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("== Servidor ==");

        try (ServerSocket serverSocket = new ServerSocket(7001)) {
            System.out.println("Aguardando conexão...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());

                Thread clienteThread = new Thread(() -> {
                    try (DataInputStream dis = new DataInputStream(socket.getInputStream());
                         DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

                        while (true) {
                            String comando = dis.readUTF();
                            switch (comando) {
                                case "readdir":
                                    dos.writeUTF(readdir());
                                    break;
                                case "rename":
                                    String antigoNome = dis.readUTF();
                                    String novoNome = dis.readUTF();
                                    rename(antigoNome, novoNome);
                                    break;
                                case "create":
                                    String novoArquivo = dis.readUTF();
                                    create(novoArquivo);
                                    break;
                                case "remove":
                                    String arquivoRemover = dis.readUTF();
                                    remove(arquivoRemover);
                                    break;
                                default:
                                    dos.writeUTF("Comando inválido.");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                clienteThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readdir() {
        StringBuilder resultado = new StringBuilder();
        for (String arquivo : arquivos) {
            resultado.append(arquivo).append("\n");
        }
        return resultado.toString();
    }

    private static void rename(String antigoNome, String novoNome) {
        if (arquivos.contains(antigoNome)) {
            arquivos.remove(antigoNome);
            arquivos.add(novoNome);
        }
    }

    private static void create(String novoArquivo) {
        arquivos.add(novoArquivo);
    }

    private static void remove(String arquivoRemover) {
        arquivos.remove(arquivoRemover);
    }
}
