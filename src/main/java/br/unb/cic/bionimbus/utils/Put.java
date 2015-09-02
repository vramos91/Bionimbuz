package br.unb.cic.bionimbus.utils;

import java.io.File;
import java.io.IOException;

import br.unb.cic.bionimbus.services.storage.bandwidth.BandwidthCalculator;
import br.unb.cic.bionimbus.services.storage.compress.CompressPolicy;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * Classe com os metodos para a realização de um upload na federação
 * 
 * @author Deric
 */
public class Put {

	private static final Long MIN_SIZE_FOR_COMPRESSION = 10 * 1024 * 1024L;
	private JSch jsch = new JSch();
	private Session session = null;
	private String address;
	private String USER = "zoonimbus";
	private String PASSW = "Zoonimbus1";
	private int PORT = 22;
	private Channel channel;
	private String path;

	public Put(String address, String path) {
		this.address = address;
		this.path = path;
	}

	public Put() {
		throw new UnsupportedOperationException("Not supported yet."); // To
																		// change
																		// body
																		// of
																		// generated
																		// methods,
																		// choose
																		// Tools
																		// |
																		// Templates.
	}

	/**
	 * Método que realiza a conexão entre o cliente e o servidor ou entre
	 * servidores, para upar um arquivo em um peer.
	 * 
	 * @return - true se o upload foi realizado com sucesso, false caso
	 *         contrário
	 * @throws JSchException
	 * @throws SftpException
	 */
	public boolean startSession() throws JSchException, SftpException {
		String pathDest = "/home/zoonimbus/zoonimbusProject/data-folder/";
		try {

			session = jsch.getSession(USER, address, PORT);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(PASSW);
			session.connect();
		} catch (JSchException e) {
			return false;
		}

		long inicio =0, fim = 0;
		String toBeSent = path;
		try {
			this.channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;
			
			inicio =System.currentTimeMillis();
			if (new File(toBeSent).getTotalSpace() >= MIN_SIZE_FOR_COMPRESSION) {
				try {

					System.out.println("\n Compressing file.....\n\n\n");
					toBeSent = CompressPolicy.verifyAndCompress(path,
							BandwidthCalculator.linkSpeed(address));
				} catch (IOException e) {
					toBeSent = path;
				}
			}
			/*
			 * Sem setar nenhuma permissao o arquivo chega trancado no destino,
			 * sendo acessado apenas pelo root, portanto preferi dar um 777
			 * antes de enviar o arquivo para que chegue livre ao destino. Por
			 * questões de segurança, talvez isso deva ser repensado
			 * futuramente.
			 */
			// sftpChannel.chmod(777, path);
			System.out.println("\n Uploading file.....\n\n\n");
			sftpChannel.put(toBeSent, pathDest);
			sftpChannel.exit();
			session.disconnect();
			fim = System.currentTimeMillis();

			CompressPolicy.deleteIfCompressed(toBeSent);

		} catch (JSchException a) {
			return false;
		}
		System.out.println("\n\n\n\n\n\n");
		System.out.println("Tempo gasto: " + (fim -inicio));
		System.out.println("Arquivo enviado: " + toBeSent);
		System.out.println("\n\n\n\n\n\n");
		
		return true;

	}
}
