/*Alterado*/
create table Funcionarios(
	cpf varchar(12) primary key,
	telefone_1 varchar(11),
	telefone_2 varchar(11),
	nome varchar(50),	
	vendedor_responsavel bool,
	chefia bool,
	ativo bool 
);


/*Alterado*/
create table Estoquista(
	cpf varchar(12) primary key,
	dataUltimoInventario date,
	acessoEstoque varchar(30),
	foreign key (cpf) references Funcionarios(cpf)
);

/*Alterado*/
create table Vendedor(
	cpf varchar(12) primary key,
	numVenda integer default 0,
	foreign key (cpf) references Funcionarios(cpf)
);


/*Alterado*/
create table Caixa(
	cpf varchar(12) primary key,
	login varchar(30) unique,
	senha varchar(30),
	foreign key (cpf) references Funcionarios(cpf)
);


/*Alterado*/
create table Produto(
	codigo integer auto_increment primary key,
	nome varchar(50),
	cor_primaria varchar(15),
	cor_secundaria varchar(15),
	preco double,
	qtdProduto integer
);

create table Diversos(
	codigo integer primary key,
	foreign key (codigo) references Produto(codigo)
);

/*Alterado*/
create table Vestuario(
	codigo integer primary key,
	genero varchar(15),
	tamanho varchar(15),
	faixa_etaria varchar(15),
	foreign key (codigo) references Produto(codigo)
);

/*Alterado*/
create table Calcados(
	codigo integer primary key,
	genero varchar(15),
	tamanho int,
	faixa_etaria varchar(15),
	foreign key (codigo) references Produto(codigo)
);

create table Fornecedor(
	cnpj varchar(20) primary key,
	nome varchar(50),
	transportaadora varchar(20)
);

create table Estoque_produto(
	id_estoque integer auto_increment primary key,
	codigo_produto integer,
	quantidade_produtos integer,
	foreign key (codigo_produto) references Produto(codigo)
	
);

/*Alterado*/
create table Cliente(
	cpf varchar(12) primary key,
	nome varchar(50),
	interesse1 varchar(100),
	interesse2 varchar(100),
	data_nascimento date,
	cidade varchar(50),
	bairro varchar(50),
	rua varchar(90),
	numero int,
	cep varchar(8),
	complemento varchar(30)
);

/*Alterado*/
create table Requisita(
	codigo_req integer auto_increment primary key,
	estoquista_cpf varchar(12),
	codigo_produto integer,
	fornecedor_cnpj varchar(20),
	qtdProduto integer,
	foreign key (estoquista_cpf) references Estoquista(cpf),
	foreign key (codigo_produto) references Produto(codigo),
	foreign key (fornecedor_cnpj) references Fornecedor(cnpj)
);

/*Alterado*/
create table Venda(
	idVenda integer auto_increment primary key,
	vendedor_cpf varchar(12),
	cliente_cpf varchar(12),
	dataVenda date,
	valorSubtotal double,
	foreign key (vendedor_cpf) references Vendedor(cpf),
	foreign key (cliente_cpf) references Cliente(cpf)
);


/*Alterado*/
create table Pagamento(
	idPagamento integer auto_increment primary key,
	forma_pag varchar(15),
	nota_fiscal varchar(20),
	caixa_cpf varchar(12),
	idVenda integer,
	valorTotal double,
	desconto double,
	foreign key (caixa_cpf) references Caixa(cpf),
	foreign key (idVenda) references Venda(idVenda)
);

/*Alterado*/
create table Armazena(
	estoquista_cpf varchar(12),
	codigo_produto integer,
	qtdArmazenada integer,
	armazena_id varchar(12) primary key,
	foreign key (estoquista_cpf) references Estoquista(cpf),
	foreign key (codigo_produto) references Produto(codigo)
);


/*Alterado*/
create table Devolucao_cliente(
	id_dev_cliente integer auto_increment primary key,
	codigo_produto integer,
	cliente_cpf varchar(12),
	vendedor_cpf varchar(12),
	devData date,
	qtdProduto integer,
	foreign key (codigo_produto) references Produto(codigo),
	foreign key (cliente_cpf) references Cliente(cpf),
	foreign key (vendedor_cpf) references Vendedor(cpf)
);


/*Alterado*/
create table Devolucao_fornecedor(
	id_dev_fornecedor integer auto_increment primary key,
	estoquista_cpf varchar(12),
	fornecedor_cnpj varchar(20),
	codigo_produto integer,
	devData date,
	qtdProduto integer,
	foreign key (estoquista_cpf) references Estoquista(cpf),
	foreign key (fornecedor_cnpj) references Fornecedor(cnpj),
	foreign key (codigo_produto) references Produto(codigo)
);

/*Alterado*/
create table Repoe(
    id_estoque_produto integer,
    id_dev_cliente integer,
    estoquista_cpf varchar(12),
    foreign key (id_estoque_produto) references Estoque_produto(id_estoque),
    foreign key (estoquista_cpf) references Estoquista(cpf),
    foreign key (id_dev_cliente) references Devolucao_cliente(id_dev_cliente)
);

/*Adicionada*/
create table Venda_item(
	idVendaItem integer auto_increment primary key,
	qtdVendaItem integer,
	codigo_produto integer,
	idVenda integer,
	foreign key (codigo_produto) references Produto(codigo),
	foreign key (idVenda) references Venda(idVenda)
);




/*trigger 1*/
DELIMITER $$

CREATE TRIGGER trg_atualiza_estoque
AFTER INSERT ON Armazena
FOR EACH ROW
BEGIN
  DECLARE qtd_atual INT;

  SELECT quantidade_produtos INTO qtd_atual
  FROM Estoque_produto
  WHERE codigo_produto = NEW.codigo_produto;

  IF qtd_atual IS NOT NULL THEN
    UPDATE Estoque_produto
    SET quantidade_produtos = quantidade_produtos + NEW.qtdArmazenada
    WHERE codigo_produto = NEW.codigo_produto;

  ELSE
    INSERT INTO Estoque_produto (codigo_produto, quantidade_produtos)
    VALUES (NEW.codigo_produto, NEW.qtdArmazenada);
  END IF;
END$$

DELIMITER ;

/*trigger 2*/
DELIMITER $$

CREATE TRIGGER trg_atualiza_estoque_venda
BEFORE INSERT ON Venda_item
FOR EACH ROW
BEGIN
  DECLARE qtd_atual INT;

  -- Obtém a quantidade atual do produto no estoque
  SELECT quantidade_produtos INTO qtd_atual
  FROM Estoque_produto
  WHERE codigo_produto = NEW.codigo_produto;

  -- Verifica se há estoque suficiente
  IF qtd_atual IS NULL THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Produto não encontrado no estoque';
  ELSEIF qtd_atual < NEW.qtdVendaItem THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Estoque insuficiente para a venda';
  ELSE
    -- Atualiza a quantidade no estoque
    UPDATE Estoque_produto
    SET quantidade_produtos = quantidade_produtos - NEW.qtdVendaItem
    WHERE codigo_produto = NEW.codigo_produto;
  END IF;
END$$

DELIMITER ;

/*trigger 3*/
DELIMITER $$

CREATE TRIGGER trg_atualiza_estoque_reposicao
AFTER INSERT ON Repoe
FOR EACH ROW
BEGIN
  DECLARE qtd_dev INT;
  DECLARE cod_prod INT;

  -- Buscar a quantidade e o código do produto devolvido
  SELECT qtdProduto, codigo_produto
  INTO qtd_dev, cod_prod
  FROM Devolucao_cliente
  WHERE id_dev_cliente = NEW.id_dev_cliente;

  -- Verifica se o produto está registrado no estoque
  IF EXISTS (
    SELECT 1
    FROM Estoque_produto
    WHERE id_estoque = NEW.id_estoque_produto
  ) THEN
    -- Atualiza a quantidade de produtos no estoque
    UPDATE Estoque_produto
    SET quantidade_produtos = quantidade_produtos + qtd_dev
    WHERE id_estoque = NEW.id_estoque_produto;
  END IF;

END$$

DELIMITER ;

/*trigger 4*/
DELIMITER $$

CREATE TRIGGER trg_atualiza_estoque_fornecedor
AFTER INSERT ON Devolucao_fornecedor
FOR EACH ROW
BEGIN
  DECLARE qtd_estoque INT;

  -- Busca a quantidade atual
  SELECT quantidade_produtos INTO qtd_estoque
  FROM Estoque_produto
  WHERE codigo_produto = NEW.codigo_produto
  LIMIT 1;

  -- Só subtrai se houver quantidade suficiente
  IF qtd_estoque IS NOT NULL AND qtd_estoque >= NEW.qtdProduto THEN
    UPDATE Estoque_produto
    SET quantidade_produtos = quantidade_produtos - NEW.qtdProduto
    WHERE codigo_produto = NEW.codigo_produto
    LIMIT 1;
  END IF;
END$$

DELIMITER ;

/*trigger 5*/
DELIMITER $$

CREATE TRIGGER trg_atualiza_numVenda
AFTER INSERT ON Venda
FOR EACH ROW
BEGIN
  
  UPDATE Vendedor
  SET numVenda = numVenda + 1
  WHERE cpf = NEW.vendedor_cpf;
END$$

DELIMITER ;

/*trigger 6*/
DELIMITER $$

CREATE TRIGGER trg_atualiza_subTotal
AFTER INSERT ON venda_item
FOR EACH ROW
BEGIN
  UPDATE Venda
  SET valorSubtotal = IFNULL(valorSubtotal, 0) + 
                      (NEW.qtdVendaItem * 
                       COALESCE((SELECT preco FROM produto WHERE codigo = NEW.codigo_produto), 0))
  WHERE idVenda = NEW.idVenda;
END$$

DELIMITER ;

/*trigger 7*/
DELIMITER $$

CREATE TRIGGER trg_atualiza_valorTotal
BEFORE INSERT ON pagamento
FOR EACH ROW
BEGIN
  DECLARE v_subtotal DOUBLE;

  SELECT valorSubtotal INTO v_subtotal
  FROM venda
  WHERE idVenda = NEW.idVenda;

  SET NEW.valorTotal = v_subtotal - NEW.desconto;
END$$

DELIMITER ;


/*Função Ativo*/
DELIMITER $$

CREATE FUNCTION verificaFuncionarioAtivo(cpfP VARCHAR(12))
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
  DECLARE ativo_func BOOLEAN;
  SELECT ativo INTO ativo_func
  FROM Funcionarios f
  WHERE f.cpf = cpfP;

  RETURN ativo_func;
END$$

DELIMITER ;


-- Funcionário 1 - Estoquista
INSERT INTO Funcionarios (cpf, telefone_1, telefone_2, nome, vendedor_responsavel, chefia, ativo)
VALUES ('11111111111', '81999990001', '81999990002', 'Carlos Silva', false, false, true);

INSERT INTO Estoquista (cpf, dataUltimoInventario, acessoEstoque)
VALUES ('11111111111', '2025-04-01', 'Completo');

-- Funcionário 2 - Vendedor
INSERT INTO Funcionarios (cpf, telefone_1, telefone_2, nome, vendedor_responsavel, chefia, ativo)
VALUES ('22222222222', '81999990003', '81999990004', 'Fernanda Lima', true, false, true);

INSERT INTO Vendedor (cpf, numVenda)
VALUES ('22222222222', 15);

-- Funcionário 3 - Caixa
INSERT INTO Funcionarios (cpf, telefone_1, telefone_2, nome, vendedor_responsavel, chefia, ativo)
VALUES ('33333333333', '81999990005', '81999990006', 'João Pedro', false, false, true);

INSERT INTO Caixa (cpf, login, senha)
VALUES ('33333333333', 'joaopedro', 'senha123');

-- Funcionário 4 - Vendedor com chefia
INSERT INTO Funcionarios (cpf, telefone_1, telefone_2, nome, vendedor_responsavel, chefia, ativo)
VALUES ('44444444444', '81999990007', '81999990008', 'Mariana Rocha', true, true, true);

INSERT INTO Vendedor (cpf, numVenda)
VALUES ('44444444444', 25);

-- Funcionário 5 - Estoquista
INSERT INTO Funcionarios (cpf, telefone_1, telefone_2, nome, vendedor_responsavel, chefia, ativo)
VALUES ('55555555555', '81999990009', '81999990010', 'Ricardo Alves', false, false, true);

INSERT INTO Estoquista (cpf, dataUltimoInventario, acessoEstoque)
VALUES ('55555555555', '2025-03-20', 'Parcial');


-- Produto 1: Camiseta esportiva (vestuário)
INSERT INTO Produto (nome, cor_primaria, cor_secundaria, preco, qtdProduto)
VALUES ('Camiseta Dry Fit Nike', 'Preto', 'Cinza', 79.90, 50);
-- Pegando o último ID gerado
INSERT INTO Vestuario (codigo, genero, tamanho, faixa_etaria)
VALUES (LAST_INSERT_ID(), 'Unissex', 'M', 'Adulto');

-- Produto 2: Tênis de corrida (calçado)
INSERT INTO Produto (nome, cor_primaria, cor_secundaria, preco, qtdProduto)
VALUES ('Tênis Adidas Run Falcon', 'Azul', 'Branco', 299.90, 30);
INSERT INTO Calcados (codigo, genero, tamanho, faixa_etaria)
VALUES (LAST_INSERT_ID(), 'Masculino', 42, 'Adulto');

-- Produto 3: Short de treino feminino (vestuário)
INSERT INTO Produto (nome, cor_primaria, cor_secundaria, preco, qtdProduto)
VALUES ('Short Esportivo Feminino Puma', 'Rosa', 'Preto', 89.90, 40);
INSERT INTO Vestuario (codigo, genero, tamanho, faixa_etaria)
VALUES (LAST_INSERT_ID(), 'Feminino', 'G', 'Adulto');

-- Produto 4: Meião de futebol (diversos)
INSERT INTO Produto (nome, cor_primaria, cor_secundaria, preco, qtdProduto)
VALUES ('Meião de Futebol Penalty', 'Vermelho', 'Branco', 29.90, 100);
INSERT INTO Diversos (codigo)
VALUES (LAST_INSERT_ID());

-- Produto 5: Chuteira Society (calçado)
INSERT INTO Produto (nome, cor_primaria, cor_secundaria, preco, qtdProduto)
VALUES ('Chuteira Nike Society Phantom', 'Preto', 'Verde', 399.90, 20);
INSERT INTO Calcados (codigo, genero, tamanho, faixa_etaria)
VALUES (LAST_INSERT_ID(), 'Masculino', 41, 'Adulto');


-- Fornecedores (3 inserções)
INSERT INTO Fornecedor (cnpj, nome, transportaadora)
VALUES ('12345678000100', 'Nike do Brasil Ltda', 'JadLog');

INSERT INTO Fornecedor (cnpj, nome, transportaadora)
VALUES ('98765432000199', 'Adidas Sports Ltda', 'FedEx');

INSERT INTO Fornecedor (cnpj, nome, transportaadora)
VALUES ('45678912000155', 'Penalty Brasil', 'Correios');

-- Clientes (2 inserções)
INSERT INTO Cliente (cpf, nome, interesse1, interesse2, data_nascimento, cidade, bairro, rua, numero, cep, complemento)
VALUES ('11122233344', 'Gilvani Souza', 'Tênis de Corrida', 'Relógios Esportivos', '1998-06-15', 'Recife', 'Boa Viagem', 'Rua das Palmeiras', 120, '51020010', 'Ap 402');

INSERT INTO Cliente (cpf, nome, interesse1, interesse2, data_nascimento, cidade, bairro, rua, numero, cep, complemento)
VALUES ('55566677788', 'Juliana Souza', 'Roupas Fitness', 'Chuteiras', '2000-03-10', 'Olinda', 'Casa Caiada', 'Av. Getúlio Vargas', 850, '53020020', 'Casa B');

-- Estoque_produto (assumindo códigos já inseridos anteriormente para produtos esportivos)
-- Suponha que os códigos dos produtos anteriores são: 1 a 5
INSERT INTO Estoque_produto (codigo_produto, quantidade_produtos)
VALUES (1, 20);

INSERT INTO Estoque_produto (codigo_produto, quantidade_produtos)
VALUES (2, 15);

INSERT INTO Estoque_produto (codigo_produto, quantidade_produtos)
VALUES (3, 25);

INSERT INTO Estoque_produto (codigo_produto, quantidade_produtos)
VALUES (4, 50);

INSERT INTO Estoque_produto (codigo_produto, quantidade_produtos)
VALUES (5, 10);


INSERT INTO Requisita (estoquista_cpf, codigo_produto, fornecedor_cnpj, qtdProduto)
VALUES ('11111111111', 1, '12345678000100', 20);

INSERT INTO Requisita (estoquista_cpf, codigo_produto, fornecedor_cnpj, qtdProduto)
VALUES ('55555555555', 2, '98765432000199', 10);

INSERT INTO Requisita (estoquista_cpf, codigo_produto, fornecedor_cnpj, qtdProduto)
VALUES ('11111111111', 3, '45678912000155', 15);


INSERT INTO Venda (vendedor_cpf, cliente_cpf, dataVenda, valorSubtotal)
VALUES ('22222222222', '11122233344', '2025-05-20', 299.90);

INSERT INTO Venda (vendedor_cpf, cliente_cpf, dataVenda, valorSubtotal)
VALUES ('44444444444', '55566677788', '2025-05-21', 479.80);


INSERT INTO Pagamento (forma_pag, nota_fiscal, caixa_cpf, idVenda, valorTotal, desconto)
VALUES ('Crédito', 'NF001234', '33333333333', 1, 299.90, 0.00);

INSERT INTO Pagamento (forma_pag, nota_fiscal, caixa_cpf, idVenda, valorTotal, desconto)
VALUES ('Pix', 'NF001235', '33333333333', 2, 479.80, 20.00);


INSERT INTO Armazena (estoquista_cpf, codigo_produto, qtdArmazenada, armazena_id)
VALUES ('11111111111', 1, 30, 'ARMZ001');

INSERT INTO Armazena (estoquista_cpf, codigo_produto, qtdArmazenada, armazena_id)
VALUES ('55555555555', 3, 20, 'ARMZ002');

INSERT INTO Devolucao_cliente (codigo_produto, cliente_cpf, vendedor_cpf, devData, qtdProduto)
VALUES (1, '11122233344', '22222222222', '2025-05-22', 1);

INSERT INTO Devolucao_cliente (codigo_produto, cliente_cpf, vendedor_cpf, devData, qtdProduto)
VALUES (2, '55566677788', '44444444444', '2025-05-23', 2);

INSERT INTO Devolucao_fornecedor (estoquista_cpf, fornecedor_cnpj, codigo_produto, devData, qtdProduto)
VALUES ('11111111111', '12345678000100', 1, '2025-05-24', 5);


INSERT INTO Repoe (id_estoque_produto, id_dev_cliente, estoquista_cpf)
VALUES (1, 1, '11111111111');

INSERT INTO Repoe (id_estoque_produto, id_dev_cliente, estoquista_cpf)
VALUES (2, 2, '55555555555');


INSERT INTO Venda_item (qtdVendaItem, codigo_produto, idVenda)
VALUES (1, 1, 1);

INSERT INTO Venda_item (qtdVendaItem, codigo_produto, idVenda)
VALUES (2, 2, 2);
