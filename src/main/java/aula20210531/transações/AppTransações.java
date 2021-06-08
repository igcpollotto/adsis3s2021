package aula20210531.transações;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.UUID;

import javax.swing.JOptionPane;

public class AppTransações {
	
	public static void main(String[] args) {
		try {
//			Connection conexão = DriverManager.getConnection("jdbc:h2:~/transações2", "sa", "");
//			conexão.close();
			Connection conexão = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/transações2", "sa", "");
			conexão.setAutoCommit(false);
			
			PreparedStatement psCreate = conexão.prepareStatement("create table if not exists livro ("
					+ "id char(36) primary key,"
					+ "titulo varchar(255) unique not null,"
					+ "ainda_publicado boolean not null,"
					+ "preco numeric(15,2) not null,"
					+ "numero_de_paginas numeric(8) not null,"
					+ "publicado_em date not null"
					+ ")");
			psCreate.execute();
			psCreate.close();			
			conexão.commit();
			
			for (int i = 0; i < 1000; i++) {
				inserirUmLivro( 
						conexão,
						UUID.randomUUID().toString(),
						"Java Como Programar: " + System.nanoTime(),
						true,
						125.77,
						275,
						new Date());
			}
			//JOptionPane.showMessageDialog(null, "Opa, mil inserts realizados, mas ainda sem commit!");
			int opção = JOptionPane.showConfirmDialog(null, "Deseja confirmar a transação?","Confirme",JOptionPane.YES_NO_OPTION);
			if (opção == JOptionPane.YES_OPTION) {
				conexão.commit();				
				System.out.println("Commit efetuado!");
			} else if (opção == JOptionPane.NO_OPTION) {
				conexão.rollback();
				System.out.println("Rollback efetuado!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Foi.");
	}


	
	private static void inserirUmLivro(
			Connection conexão,
			String id,
			String título,
			boolean aindaPublicado,
			double preço, 
			int númeroDePáginas,
			Date publicadoEm) {
		
		try {
			PreparedStatement psInsert = conexão.prepareStatement("insert into livro "
					+ " (id, titulo, ainda_publicado, preco, numero_de_paginas, publicado_em) "
					+ " values "
					+ " ( ?,      ?,              ?,     ?,                 ?,           ?)");
			psInsert.setString(1, id);
			psInsert.setString(2, título);
			psInsert.setBoolean(3, aindaPublicado);
			psInsert.setDouble(4, preço);
			psInsert.setInt(5, númeroDePáginas);
			psInsert.setDate(6, new java.sql.Date(publicadoEm.getTime()));
			psInsert.execute();
			psInsert.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
}

















