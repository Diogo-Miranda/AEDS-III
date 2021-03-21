# CRUD Genérico para manipulação de arquivos com índice direto em ID - Class Comentarios :rocket:

- CRUD feito para atender a manipulação de arquivos como memória externa
- Os metadados do arquivo se resume em 1 inteiro armazenando o maior ID presente nos registros.

1. Interface Registro
  > A interface registro é responsável por criar um escopo
  > para a futura manipulação de dos registros por outra classe.
  
  O método **toByteArray()** deve ser implementado pelas classes que implementam a interface registro,
  essa é responsável por transformar um objeto em um vetor de bytes.
  O método **fromByteArray(byte[] ba)** deve ser implementado pelas classes que implementam a interace registro,
  essa é responsável por receber um vetor de bytes e transformar em um objeto.  
  
2. Estrutura dos Registros
  > * Os registros estão organizados utilizando uma estrutura de dados de hash extensível, sendo referênciados em um **índice primário direto**
  > * Todas as operações são feitas encima do atributo de ID - índice primário
  > * O arquivo de índice guarda a informção <ID, Endereço> que é utilizada nas operações do CRUD
  > * O hash é feitro encima do atributo ID, pelo método **hashCode()**
  > * Na interface de RegistroHash, foi adicionado o método **getEndereco()**, que retorna o endereço no arquivo de dados do registro a partir do arquivo de índices.

3. Classe CRUD
  > Por sua vez, a classe CRUD possuí um construtor genérico que recebe o construtor o objeto a ser operado e também uma string
  > indicando o caminho de abertura do arquivo.
  
  * **create(T objeto)**
  > * O método create() recebe um objeto T, e escreve no arquivo os registros na estrutura indicado em Estrutura dos Registros. 
  > * Retorna o ID gerado e atribuído ao objeto.
  
  * **update(T objeto)**
  > * Recebe um objeto T, realizar uma **pesquisa sequencial** no arquivo até encontrar EOF ou encontrar o ID do registro que irá ser alterado
  > * Se **objeto.length() == registro.length()** - objeto recebido é escrito nesse mesmo espaço
  > * Se **objeto.length() > registro.length()** - o objeto recebido é escrito no final do arquivo e o registro é dado como apagado (lápide = 1)
  > * Se **objeto.length() < registro.length()** - o objeto é escrito no espaço, mas o ponteiro para o próximo registro é aproveitado. O restante dos bytes são dados como lixo
 
  * **delete(int ID)**
  > * É feita uma **pesquisa sequencial** no arquivo até encontrar EOF ou encontrar o ID do registro que irá ser excluíodo
  > * Quando encontra o ID, a lápide do registro é dada como 1 e é retornado *true*; se o registro não for encontrado é retornado *false*
  
  * **read(int ID)**
  > * É feita uma **pesquisa sequencial** no arquivo até encontrar EOF ou encontrar o ID do registro que é procurado
  > * Quando achado o registro, ele é instanciado e retornado pelo método.
  > * Se não encontrar, retonra *null*]

4. Interface de iteração
  > Na classe Main é possível manipular as operações a partir de uma iteração pelo terminal, em que as opções
  > são escolhidas de acordo com a operação desejada.
  > Ao final da realização da operação basta digitar '0' para fechar o programa,
