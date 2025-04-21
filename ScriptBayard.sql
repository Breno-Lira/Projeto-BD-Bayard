create table Funcionarios(
	cpf varchar(12) primary key,
	telefone varchar(11),
	nome varchar(50),	
	vendedor_responsavel bool,
	chefia bool
);

create table Estoquista(
	cpf varchar(12) primary key,
	dataUltimoInventario date,
	acessoEstoque varchar(30),
	foreign key (cpf) references Funcionarios(cpf)
);

create table Vendedor(
	cpf varchar(12) primary key,
	numVenda integer,
	foreign key (cpf) references Funcionarios(cpf)
);

create table Caixa(
	cpf varchar(12) primary key,
	login varchar(20),
	senha varchar(10),
	foreign key (cpf) references Funcionarios(cpf)
);

create table Produto(
	codigo varchar(5) primary key,
	nome varchar(50),
	cor varchar(15),
	preco double
);

create table Diversos(
	codigo varchar(5) primary key,
	foreign key (codigo) references Produto(codigo)
);

create table Vestuario(
	codigo varchar(5) primary key,
	genero char,
	tamanho varchar(2),
	faixa_etaria varchar(2),
	foreign key (codigo) references Produto(codigo)
);

create table Calcados(
	codigo varchar(5) primary key,
	genero char,
	tamanho int,
	faixa_etaria varchar(2),
	foreign key (codigo) references Produto(codigo)
);

create table Fornecedor(
	cnpj varchar(20) primary key,
	nome varchar(50),
	transportaadora varchar(20)
);

create table Estoque_produto(
	id_estoque varchar(30) primary key,
	codigo_produto varchar(5),
	quantidade_produtos int,
	foreign key (codigo_produto) references Produto(codigo)
	
);

create table Cliente(
	cpf varchar(12) primary key,
	nome varchar(50),
	interesse varchar(100),
	data_nascimento date,
	cidade varchar(58),
	bairro varchar(85),
	rua varchar(85),
	numero int,
	cep varchar(8),
	complemento varchar(30)
);

create table Requisita(
	codigo_req varchar(5) primary key,
	estoquista_cpf varchar(12),
	codigo_produto varchar(5),
	fornecedor_cnpj varchar(20),
	foreign key (estoquista_cpf) references Estoquista(cpf),
	foreign key (codigo_produto) references Produto(codigo),
	foreign key (fornecedor_cnpj) references Fornecedor(cnpj)
);

create table Venda(
	idVenda varchar(20) primary key,
	dataVenda date,
	valorSubtotal double,
	vendedor_cpf varchar(12),
	codigo_produto varchar(5) ,
	cliente_cpf varchar(12) ,
	foreign key (vendedor_cpf) references Vendedor(cpf),
	foreign key (codigo_produto) references Produto(codigo),
	foreign key (cliente_cpf) references Cliente(cpf)
);



create table Pagamento(
	forma_pag varchar(15),
	nota_fiscal varchar(20),
	caixa_cpf varchar(12),
	venda_id varchar(5),
	valorTotal double,
	desconto double,
	foreign key (caixa_cpf) references Caixa(cpf),
	foreign key (venda_id) references Venda(idVenda)
);


create table Armazena(
	estoquista_cpf varchar(12),
	codigo_produto varchar(5),
	foreign key (estoquista_cpf) references Estoquista(cpf),
	foreign key (codigo_produto) references Produto(codigo)
);

create table Devolucao_cliente(
	id_dev varchar(20) primary key,
	codigo_produto varchar(5) unique,
	cliente_cpf varchar(12),
	vendedor_cpf varchar(12),
	dataDevolucao date,
	foreign key (codigo_produto) references Produto(codigo),
	foreign key (cliente_cpf) references Cliente(cpf),
	foreign key (vendedor_cpf) references Vendedor(cpf)
);


create table Devolucao_fornecedor(
	id_dev_fornecedor varchar(30) primary key,
	estoquista_cpf varchar(12),
	fornecedor_cnpj varchar(20),
	codigo_produto varchar(5),
	dataDevolucao date,
	foreign key (estoquista_cpf) references Estoquista(cpf),
	foreign key (fornecedor_cnpj) references Fornecedor(cnpj),
	foreign key (codigo_produto) references Produto(codigo)
);

create table Repoe(
	id_estoque_produto varchar(30),
	codigo_produto varchar(5),
	estoquista_cpf varchar(12),
	foreign key (id_estoque_produto) references Estoque_produto(id_estoque),
	foreign key (estoquista_cpf) references Estoquista(cpf),
	foreign key (codigo_produto) references Devolucao_cliente(codigo_produto)
);

-- 1. Inserir Funcionários
INSERT INTO Funcionarios (cpf, telefone, nome, vendedor_responsavel, chefia)
VALUES
('11111111111', '11999999999', 'Carlos Silva', true, false),
('22222222222', '11888888888', 'Ana Souza', false, true),
('33333333333', '11777777777', 'Bruno Lima', false, false);

-- 2. Inserir Estoquista
INSERT INTO Estoquista (cpf, dataUltimoInventario, acessoEstoque)
VALUES
('22222222222', '2025-04-01', 'Estoque Geral');

-- 3. Inserir Vendedor
INSERT INTO Vendedor (cpf, numVenda)
VALUES
('11111111111', 0);

-- 4. Inserir Caixa
INSERT INTO Caixa (cpf, login, senha)
VALUES
('33333333333', 'caixa1', '123456');

-- 5. Inserir Produto
INSERT INTO Produto (codigo, nome, cor, preco)
VALUES
('P001', 'Camiseta Branca', 'Branco', 49.90),
('P002', 'Tênis Esportivo', 'Preto', 199.90);

-- 6. Inserir Vestuário
INSERT INTO Vestuario (codigo, genero, tamanho, faixa_etaria)
VALUES
('P001', 'M', 'G', '18');

-- 7. Inserir Calçados
INSERT INTO Calcados (codigo, genero, tamanho, faixa_etaria)
VALUES
('P002', 'M', 42, '18');

-- 8. Inserir Fornecedor
INSERT INTO Fornecedor (cnpj, nome, transportaadora)
VALUES
('12345678000199', 'Fornecedor XYZ', 'TransXP');

-- 9. Inserir Cliente
INSERT INTO Cliente (cpf, nome, interesse, data_nascimento, cidade, bairro, rua, numero, cep, complemento)
VALUES
('44444444444', 'João Oliveira', 'Moda Casual', '1990-06-15', 'São Paulo', 'Centro', 'Rua das Flores', 123, '01010000', 'Apto 12');

-- 10. Inserir Venda
INSERT INTO Venda (idVenda, dataVenda, valorSubtotal, vendedor_cpf, codigo_produto, cliente_cpf)
VALUES
('V001', '2025-04-20', 49.90, '11111111111', 'P001', '44444444444');









