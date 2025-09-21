# Microservice Para Gerenciamento Financeiro

### FINANCEIRO-SERVICE

Modulo financeiro corporativo desenvolvido com arquitetura de microserviços, permitindo escalabilidade, manutenibilidade

e integração com serviços externos via APIs REST. A aplicação é hospedada em ambiente cloud, garantindo

alta disponibilidade e segurança dos dados. Entre suas funcionalidades estão o controle de contas, fluxo de caixa,

conciliação bancária automatizada, relatórios em tempo real e integração contábil.

#### Requisitos para execução

* openjdk21;

* maven;

* docker;

* docker-compose.

#### Instruções para execução

* Na raiz do projeto execute: mvn install;

* Na raiz do projeto, dentro da pasta Docker execute: docker-compose up -d.

#### Variáves de ambiente

Abaixo segue a lista de variaveis de ambiente necessárias a execução do projeto.

1. ENVIRONMENT - Define o profile do arquivo de propriedades. Informar dev ou hom ou prod;

2. SERVER_PORT - Define a porta de execução da aplicação no servidor;

3. DB_USERNAME - Usuário do banco de dados

4. DB_PASSWORD - Password do banco de dados

5. DB_URL - URL da conexão com o banco de dados

6. HIKARI_CONNECTION_TIMEOUT - Timeout da conexão

7. HIKARI_IDLE_TIMEOUT - Tempo de expiração de conexões ociosas

8. HIKARI_MAX_LIFETIME - 1800000

9. HIKARI_CONNECTION_TEST_QUERY - Teste para conexão ativa

10. HIKARI_MAXIMUM_POOL_SIZE - Número de conexões ociosas

11. HIKARI_MINIMUM_IDLE - Número mínimo de conexões que devem permanecer ociosas

12. HIKARI_INITIALIZATION_FAIL_TIMEOUT - Tempo em milissegundos que o Hikari espera para confirmar se a configuração do

    pool está funcional na inicialização

13. HIKARI_ISOLATE_INTERNAL_QUERIES - Indica se as consultas internas executadas pelo Hikari devem ser isoladas por uma

    transação

14. HIKARI_VALIDATION_TIMEOUT - Tempo máximo em milissegundos que o Hikari aguardará para validar uma conexão antes de

    considerá-la inválida

15. HIKARI_DATASOURCE_PROPERTIES_CACHEPREPSTMTS - Configura o cache para _prepared statements_ (estatements

    pré-compiladas para reutilização no banco)

16. HIKARI_DATASOURCE_PROPERTIES_PHOTOSYNTHESIZE - Define o tamanho do cache para _prepared statements_ no cliente

    JDBC

17. HIKARI_DATASOURCE_PROPERTIES_PREPSTMTCACHESQLLIMIT - Define o tamanho máximo (em caracteres) do SQL que será

    armazenado no cache de _prepared statements_

18. CIRCUIT_BREAKER_SLIDING_WINDOW_SIZE - Define o tamanho da janela usada para calcular as métricas do Circuit

    Breaker (como a taxa de falhas).

19. CIRCUIT_BREAKER_FAILURE_RATE_THRESHOULD - Determina o limite percentual de falhas em uma janela de monitoramento

    para que o circuito seja aberto.

20. CIRCUIT_BREAKER_PERMITTED_NUMBER_CALLS - Define o número de chamadas permitidas quando o Circuit Breaker está no

    estado meio-aberto.

21. CIRCUIT_BREAKER_WAIT_DURATION_IN_OPEN_STATE - Define a duração de tempo que o circuito permanece no estado aberto

    antes de permitir tentativas de reconexão (estado meio-aberto).

22. CIRCUIT_BREAKER_MINIMUM_NUMBER_OF_CALLS - Determina o número mínimo de chamadas que devem ocorrer antes de o Circuit

    Breaker avaliar se deve abrir ou permanecer fechado.

23. RETRY_MAX_ATTEMPTS - Número máximo de tentativas de repetição permitidas para uma operação antes que seja

    considerada uma falha.

24. RETRY_WAIT_DURATION - Tempo de espera entre tentativas de repetição.

25. LIMITER_FOR_PERIOD - Define o limite máximo de chamadas permitidas dentro de um período especificado.

26. LIMITER_REFRESH_PERIOD - Define o intervalo de tempo em que o contador de chamadas do Rate Limiter será reiniciado.

27. LIMITER_TIMEOUT_DURATIOS - Tempo máximo que uma solicitação aguardará se o Rate Limiter tiver atingido o limite.

28. DB_SERVER - Define o servidor de banco de dados. Esta environment define os scripts do Flyway corretos, executados

    no start da aplicação. Aceita os valores postgresql (PostgreSQL) ou sqlserver (Microsoft SQL Server).

29. DB_SCHEMA - Esquema do banco de dados. É aceitado os schemas dos bancos de dados. Por padrão, public para PostgreSQL

    ou dbo para Microsoft SQL Server.

#### Notificação

###### Recomenda-se a criação de um arquivo .env na pasta Docker/jar do projeto para configurar todas as variáveis de ambiente.

###### _Abaixo o exemplo de uma arquivo configurado para um profile LOCAL_.

###### _Exemplo das Environments_:

1. ENVIRONMENT - local;

2. SERVER_PORT - 9012;

3. JAR_FILE - financeiro-service.jar;

4. DB_USERNAME - postgres

5. DB_PASSWORD - postgres

6. DB_URL - jdbc:postgresql://localhost:5432/financeiro?loggerLever=error

7. HIKARI_CONNECTION_TIMEOUT - 300000

8. HIKARI_IDLE_TIMEOUT - 60000

9. HIKARI_MAX_LIFETIME - 1800000

10. HIKARI_CONNECTION_TEST_QUERY - SELECT 1

11. HIKARI_MAXIMUM_POOL_SIZE - 10

12. HIKARI_MINIMUM_IDLE - 5

13. HIKARI_INITIALIZATION_FAIL_TIMEOUT - 2000

14. HIKARI_ISOLATE_INTERNAL_QUERIES - true

15. HIKARI_VALIDATION_TIMEOUT - 3000

16. HIKARI_DATASOURCE_PROPERTIES_CACHEPREPSTMTS - true

17. HIKARI_DATASOURCE_PROPERTIES_PHOTOSYNTHESIZE - 512

18. HIKARI_DATASOURCE_PROPERTIES_PREPSTMTCACHESQLLIMIT - 2048

19. CIRCUIT_BREAKER_SLIDING_WINDOW_SIZE - 100

20. CIRCUIT_BREAKER_FAILURE_RATE_THRESHOULD - 50

21. CIRCUIT_BREAKER_PERMITTED_NUMBER_CALLS - 10

22. CIRCUIT_BREAKER_WAIT_DURATION_IN_OPEN_STATE - 10s

23. CIRCUIT_BREAKER_MINIMUM_NUMBER_OF_CALLS - 10

24. RETRY_MAX_ATTEMPTS - 2

25. RETRY_WAIT_DURATION - 2s

26. LIMITER_FOR_PERIOD - 10

27. LIMITER_REFRESH_PERIOD - 1s

28. LIMITER_TIMEOUT_DURATIOS - 5s

29. DB_SERVER - sqlserver

30. DB_SCHEMA - dbo