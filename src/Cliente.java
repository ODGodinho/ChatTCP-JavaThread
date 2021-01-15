import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Cliente extends Thread {

	protected Socket conexao;

	public Cliente(Socket conexao) {
		this.conexao = conexao;
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Cliente Ativo!");

		String nome_cliente;
		String msg_digitada;
		String assunto;

		// cria o stream do teclado
		BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Informe o nome do cliente: ");
		nome_cliente = teclado.readLine();
		
		System.out.print("Qual assunto você quer entrar: \n" + 
				"1 - Tecnologia\n" + 
				"2 - Entretenimento\n" + 
				"3 - Economia: \n");
		assunto = teclado.readLine();
		
		
		Socket cliente = new Socket("localhost", 8057);
		
		DataOutputStream saida_servidor = new DataOutputStream(cliente.getOutputStream());


		System.out.println(nome_cliente + ", você entrou no chat!");
		
		(new Cliente(cliente)).start();
		
		saida_servidor.writeBytes(nome_cliente + '\n');
		saida_servidor.writeBytes(assunto + '\n');
		
		while (true) {

			
			msg_digitada = teclado.readLine();

			if ("fim".equalsIgnoreCase(msg_digitada) == true)
				break;

			saida_servidor.writeBytes(msg_digitada + '\n');
			
		}
		
	}

	@Override
	public void run() {
		
		try {
			String msg_recebida;
			while (true) {

				BufferedReader entrada_servidor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
				msg_recebida = entrada_servidor.readLine();
				// apresenta a linha do servidor na console
				System.out.println(msg_recebida);
			
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}