import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server extends Thread {

	protected Socket conexao;

	public static ArrayList<DataOutputStream> chatEconomia;
	public static ArrayList<DataOutputStream> chatEntretenimento;
	public static ArrayList<DataOutputStream> chatTecnologia;

	public Server(Socket conexao) {
		this.conexao = conexao;
		if (chatEconomia == null) {
			chatEconomia = new ArrayList<DataOutputStream>();
			chatEntretenimento = new ArrayList<DataOutputStream>();
			chatTecnologia = new ArrayList<DataOutputStream>();
		}
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		System.out.println("Servidor Ativo!");

		ServerSocket servidor = new ServerSocket(8057);

		while (true) {

			System.out.println("Esperando cliente se conectar...");
			Socket conexao = servidor.accept();
			System.out.println("O Cliente entrou no Socket");
			System.out.println("");

			(new Server(conexao)).start();

		}

	}

	@Override
	public void run() {

		String msg_rec;
		String msg_env;

		try {

			BufferedReader entrada_cliente = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			DataOutputStream saida_cliente = new DataOutputStream(conexao.getOutputStream());

			String nome = entrada_cliente.readLine();
			String assunto = entrada_cliente.readLine();
			
			ArrayList<DataOutputStream> currentChat;
			
			String assuntoNome = "";
			switch (assunto.toString().toLowerCase().trim()) {
			case "tecnologia":
			case "1":
				assuntoNome = "Tecnologia";
				chatTecnologia.add(saida_cliente);
				currentChat = chatTecnologia;
				break;
			case "entretenimento":
			case "2":
				assuntoNome = "Entretenimento";
				chatEntretenimento.add(saida_cliente);
				currentChat = chatEntretenimento;
				break;
			case "economia":
			case "3":
				assuntoNome = "Economia";
				chatEconomia.add(saida_cliente);
				currentChat = chatEconomia;
				break;
			default:
				saida_cliente.writeBytes("Você não entrou em um chat valido, agora vai para todos!!");
				chatTecnologia.add(saida_cliente);
				chatEntretenimento.add(saida_cliente);
				chatEconomia.add(saida_cliente);
				currentChat = new ArrayList<DataOutputStream>();
				currentChat.addAll(chatTecnologia);
				currentChat.addAll(chatEntretenimento);
				currentChat.addAll(chatEconomia);
				break;
			}

			while (true) {

				msg_rec = entrada_cliente.readLine();

				if ("fim".equalsIgnoreCase(msg_rec)) {
					break;
				}
				
				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
				Date date = new Date(System.currentTimeMillis());
				
				msg_env = "<" + assuntoNome + "> " + 
						  "<" + nome + ">: <" + msg_rec + 
						  "> - " + formatter.format(date) +
						  '\n';

				for (DataOutputStream saida : currentChat) {
					saida.writeBytes(msg_env);
				}

				

			}

			System.out.println("Cliente desconectado!");
			System.out.println("");
			conexao.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("Deu erro aqui po! fudeu fudeu. fudeu");
		}

	}

}