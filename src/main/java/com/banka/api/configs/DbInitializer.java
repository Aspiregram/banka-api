package com.banka.api.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer implements CommandLineRunner {
    private final JdbcTemplate jdbcTemp;

    public DbInitializer(JdbcTemplate jdbcTemp) {
        this.jdbcTemp = jdbcTemp;
    }

    @Override
    public void run(String... args) throws Exception {
        // Mostra saldo total por usuário
        jdbcTemp.execute(
                """
                        CREATE OR REPLACE VIEW vw_saldo_usuario AS
                        SELECT
                            u.id AS usuario_id,
                            u.nome_completo,
                            o.nome AS ong_responsavel,
                            m.sigla AS moeda,
                            SUM(c.saldo) AS saldo_total
                        FROM usuario u
                        JOIN conta c ON c.usuario_id = u.id
                        JOIN moeda m ON m.id = c.moeda_id
                        JOIN ong o ON o.id = u.ong_id
                        GROUP BY u.id, u.nome_completo, o.nome, m.sigla;"""
        );

        // Relatório de doações recebidas
        jdbcTemp.execute(
                """
                        CREATE OR REPLACE VIEW vw_doacoes_ong AS
                        SELECT
                            o.nome AS ong,
                            m.sigla AS moeda,
                            COUNT(d.id) AS total_doacoes,
                            SUM(d.valor) AS total_recebido
                        FROM doacao d
                        JOIN ong o ON o.id = d.ong_destino_id
                        JOIN moeda m ON m.id = d.moeda_id
                        GROUP BY o.nome, m.sigla;"""
        );

        // Detalha transações entre contas
        jdbcTemp.execute(
                """
                        CREATE OR REPLACE VIEW vw_transacoes_detalhadas AS
                        SELECT
                            t.id AS transacao_id,
                            co.usuario_id AS usuario_origem,
                            cd.usuario_id AS usuario_destino,
                            mo.sigla AS moeda_origem,
                            md.sigla AS moeda_destino,
                            t.valor_original,
                            t.valor_convertido,
                            t.taxa_utilizada,
                            t.tipo,
                            t.status,
                            t.data_transacao
                        FROM transacao t
                        LEFT JOIN conta co ON co.id = t.conta_origem_id
                        LEFT JOIN conta cd ON cd.id = t.conta_destino_id
                        LEFT JOIN moeda mo ON mo.id = t.moeda_origem_id
                        LEFT JOIN moeda md ON md.id = t.moeda_destino_id;"""
        );

        // Converte valores entre moedas
        jdbcTemp.execute(
                """
                        DELIMITER $$
                        
                        CREATE FUNCTION fn_converter_moeda(
                            valor DECIMAL(15,2),
                            moeda_origem CHAR(36),
                            moeda_destino CHAR(36)
                        )
                        RETURNS DECIMAL(15,2)
                        DETERMINISTIC
                        BEGIN
                            DECLARE taxa_origem DECIMAL(10,4);
                            DECLARE taxa_destino DECIMAL(10,4);
                            DECLARE valor_convertido DECIMAL(15,2);
                        
                            SELECT taxa_conversao INTO taxa_origem FROM moeda WHERE id = moeda_origem;
                            SELECT taxa_conversao INTO taxa_destino FROM moeda WHERE id = moeda_destino;
                        
                            SET valor_convertido = valor * (taxa_destino / taxa_origem);
                        
                            RETURN ROUND(valor_convertido, 2);
                        END$$
                        
                        DELIMITER ;
                        
                        SELECT fn_converter_moeda(100, 'uuid_brl', 'uuid_eur');"""
        );

        // Calcula saldo consolidado de um usuário
        jdbcTemp.execute(
                """
                        DELIMITER $$
                        
                        CREATE FUNCTION fn_saldo_usuario(usuario_id CHAR(36))
                        RETURNS DECIMAL(15,2)
                        DETERMINISTIC
                        BEGIN
                            DECLARE saldo_total DECIMAL(15,2) DEFAULT 0;
                        
                            SELECT
                                SUM(c.saldo * m.taxa_conversao)
                            INTO saldo_total
                            FROM conta c
                            JOIN moeda m ON m.id = c.moeda_id
                            WHERE c.usuario_id = usuario_id;
                        
                            RETURN IFNULL(saldo_total, 0);
                        END$$
                        
                        DELIMITER ;"""
        );

        // 	Transfere valores entre contas com câmbio
        jdbcTemp.execute(
                """
                        DELIMITER $$
                        
                        CREATE PROCEDURE sp_transferir_valor(
                            IN p_conta_origem CHAR(36),
                            IN p_conta_destino CHAR(36),
                            IN p_valor DECIMAL(15,2)
                        )
                        BEGIN
                            DECLARE v_moeda_origem CHAR(36);
                            DECLARE v_moeda_destino CHAR(36);
                            DECLARE v_valor_convertido DECIMAL(15,2);
                            DECLARE v_saldo_origem DECIMAL(15,2);
                        
                            SELECT moeda_id, saldo INTO v_moeda_origem, v_saldo_origem
                            FROM conta WHERE id = p_conta_origem;
                        
                            SELECT moeda_id INTO v_moeda_destino FROM conta WHERE id = p_conta_destino;
                        
                            IF v_saldo_origem < p_valor THEN
                                SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Saldo insuficiente.';
                            END IF;
                        
                            SET v_valor_convertido = fn_converter_moeda(p_valor, v_moeda_origem, v_moeda_destino);
                        
                            UPDATE conta SET saldo = saldo - p_valor WHERE id = p_conta_origem;
                            UPDATE conta SET saldo = saldo + v_valor_convertido WHERE id = p_conta_destino;
                        
                            INSERT INTO transacao (
                                id, conta_origem_id, conta_destino_id, valor_original, valor_convertido,
                                moeda_origem_id, moeda_destino_id, taxa_utilizada, tipo, status
                            )
                            VALUES (
                                UUID(), p_conta_origem, p_conta_destino, p_valor, v_valor_convertido,
                                v_moeda_origem, v_moeda_destino,
                                (SELECT taxa_conversao FROM moeda WHERE id = v_moeda_destino),
                                'TRANSFERENCIA', 'CONCLUIDA'
                            );
                        END$$
                        
                        DELIMITER ;"""
        );

        // Atualiza saldo global da ONG
        jdbcTemp.execute(
                """
                        DELIMITER $$
                        
                        CREATE PROCEDURE sp_atualizar_saldo_global_ong(IN p_ong_id CHAR(36))
                        BEGIN
                            DECLARE v_total DECIMAL(15,2);
                        
                            SELECT
                                SUM(c.saldo * m.taxa_conversao)
                            INTO v_total
                            FROM conta c
                            JOIN usuario u ON u.id = c.usuario_id
                            JOIN moeda m ON m.id = c.moeda_id
                            WHERE u.ong_id = p_ong_id;
                        
                            UPDATE ong SET saldo_global = IFNULL(v_total, 0) WHERE id = p_ong_id;
                        END$$
                        
                        DELIMITER ;"""
        );

        // Atualiza automaticamente saldos das contas após transação
        jdbcTemp.execute(
                """
                        DELIMITER $$
                        
                        CREATE TRIGGER trg_atualiza_saldo_apos_transacao
                        AFTER INSERT ON transacao
                        FOR EACH ROW
                        BEGIN
                            DECLARE v_tipo VARCHAR(20);
                            DECLARE v_valor DECIMAL(15,2);
                        
                            SET v_tipo = NEW.tipo;
                            SET v_valor = NEW.valor_original;
                        
                            IF NEW.status = 'CONCLUIDA' THEN
                                -- Atualiza conta de origem (débito)
                                UPDATE conta
                                SET saldo = saldo - NEW.valor_original
                                WHERE id = NEW.conta_origem_id;
                        
                                UPDATE conta
                                SET saldo = saldo + NEW.valor_convertido
                                WHERE id = NEW.conta_destino_id;
                            END IF;
                        END$$
                        
                        DELIMITER ;"""
        );

        // Impede saldo negativo em qualquer atualização
        jdbcTemp.execute(
                """
                        DELIMITER $$
                        
                        CREATE TRIGGER trg_impedir_saldo_negativo
                        BEFORE UPDATE ON conta
                        FOR EACH ROW
                        BEGIN
                            IF NEW.saldo < 0 THEN
                                SIGNAL SQLSTATE '45000'
                                SET MESSAGE_TEXT = 'Operação inválida: saldo insuficiente.';
                            END IF;
                        END$$
                        
                        DELIMITER ;"""
        );
    }
}
