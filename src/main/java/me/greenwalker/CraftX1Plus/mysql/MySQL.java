package me.floydz69.CraftX1Plus.mysql;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.util.Util;

import java.sql.*;

public class MySQL extends MySQLUtils {

    //Classe singleton
    private static MySQL instance;

    private MySQL() {
        try {
            getConnection();
            createTables();
        } catch (SQLException e) {
            CraftPlus.getMain().getServer().getConsoleSender().sendMessage("§4§lErro ao se conectar ao banco de dados! setando armazenamento local");
            e.printStackTrace();
            CraftPlus.getMain().setMysql(false);
        }
    }

    public static MySQL getInstance() {
        if (instance == null) {
            instance = new MySQL();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection("jdbc:mysql://" + CraftPlus.getMain().getPConfig().getString("host") + ":" + CraftPlus.getMain().getPConfig().getString("porta") + "/" + CraftPlus.getMain().getPConfig().getString("servidor"), CraftPlus.getMain().getPConfig().getString("usuario"), CraftPlus.getMain().getPConfig().getString("senha"));
    }

    public void createTables() {
            PreparedStatement ps = null;
            Connection c = null;
        try {
                    c = getConnection();
            // Players
            ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS craftx1_player(`id` varchar(100) NOT NULL, `nick` varchar(20) NOT NULL UNIQUE, `vitorias` bigint(10) NOT NULL, `derrotas` bigint(10) NOT NULL, `bau_size` int(2) DEFAULT '0', PRIMARY KEY(`id`), UNIQUE KEY(`nick`))ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.executeUpdate();
            closeStatement(ps);

            ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS craftx1_clan(`id` bigint(10) NOT NULL AUTO_INCREMENT, `tag` varchar(10) NOT NULL UNIQUE, `vitorias` bigint(10) NOT NULL, `derrotas` bigint(10) NOT NULL,  PRIMARY KEY(`id`), UNIQUE KEY(`tag`))ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.executeUpdate();
            closeStatement(ps);

            // Batalhas
            ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS craftx1_batalhas_players(`id` bigint(100) NOT NULL AUTO_INCREMENT,`id_player` varchar(100) NOT NULL, `contra` varchar(100) NOT NULL, `venceu` bit(1) NOT NULL, `tipo` varchar(10) NOT NULL,`data` varchar(10) NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(id_player) REFERENCES craftx1_player(`id`))ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.executeUpdate();
            closeStatement(ps);

            ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS craftx1_batalhas_clans(`id` bigint(10) NOT NULL AUTO_INCREMENT, `id_clan` varchar(10) NOT NULL, `contra` varchar(10) NOT NULL, `venceu` bit(1) NOT NULL, `tipo` varchar(10) NOT NULL,`data` varchar(10) NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(id_clan) REFERENCES craftx1_clan(`tag`))ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.executeUpdate();
            closeStatement(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeStatement(ps);
            closeConnection(c);
        }
    }
}

