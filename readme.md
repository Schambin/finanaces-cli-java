# Sistema Financeiro CLI - Java

## Descrição
Aplicação de linha de comando para gerenciar contas a pagar e receber, com registro de valores, datas e status.

## Funcionalidades
- Cadastrar contas (pagar/receber)
- Listar todas as contas
- Marcar contas como pagas
- Gerar resumo financeiro
- Buscar contas por ID

## Requisitos
- Java JDK 17+
- (Opcional) Maven 3.9+

## Como Executar

### Método 1: IntelliJ
1. Abra o projeto
2. Execute `Main.java`

### Método 2: Terminal
```bash
# Compilar
javac -d out src/main/java/main/Main.java src/main/java/model/*.java src/main/java/service/*.java

# Executar
java -cp out main.Main
```
### Método 3: Com Maven
```
mvn clean compile exec:java -Dexec.mainClass="main.Main"
```

### Estrutura do Projeto
~~~
src/
├── main/
│   ├── java/
│   │   ├── model/ (Account.java, AccountType.java)
│   │   └── service/ (AccountService.java, SummaryService.java)
│   ├── Main.java
~~~