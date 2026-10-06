#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>

typedef struct Data{ //Crio a struct data
    int ano, mes, dia;
} Data;

typedef struct Veiculo{ //Crio a struct veículo
    int id, ano, cilindros;
    double cilindrada, consumoCidade, consumoEstrada, co2;
    bool turbo;
    char marca[50], modelo[50], categoria[50], transmissao[50], tracao[50];
    char combustivel[5][50];
    Data dataRegistro;
} Veiculo;

Data parseData(char* s){ //Crio o parseData para converter a string data lida do arquivo para uma variável do tipo Data
    Data data;
    sscanf(s, "%d-%d-%d", &data.ano, &data.mes, &data.dia);
    return data;
}

void formatData(Data d, char* buffer){ //formatData para printar a data do jeito que deve ser
    sprintf(buffer, "%02d/%02d/%04d", d.dia, d.mes, d.ano);
}

void parseCombustivel(char* c, Veiculo* veic){
    int i = 0, j = 0; //Crio os índices da matriz

    for(int k=0; c[k] != '\0'; k++){ //Percorre a string até encontrar o caractere terminador, o \0
        
        if(c[k] == ';'){ //Se o caractere atual for ; significa que achei o separador entre os combustíveis
            veic->combustivel[i][j] = '\0'; //Fecho essa string com \0
            i++; //Vou para o próximo combustível
            j = 0; //Começo a ler os caracteres da posição inicial
        }

        else{
            veic->combustivel[i][j] = c[k]; //Caso contrário avanço para o próximo caractere
            j++;
        }
    }
    veic->combustivel[i][j] = '\0'; //Quando sai do loop fecho o último combustível
    i++;

    for(; i<5; i++){
        veic->combustivel[i][0] = '\0'; //Limpa as posições que sobraram da matriz
    }
}

void formatCombustivel(Veiculo v, char* buffer){
    buffer[0] = '\0'; //Inicializo o buffer para conseguir usar o strcat
    bool continua = true; //Para parar o loop quando necessário

    for(int i=0; i<5 && continua; i++){
        if(v.combustivel[i][0] == '\0'){
            continua = false; //Se a posição i da matriz está vazia significa que chagou ao fim dos combustíveis e encerro o loop
        }

        else{
            if(i>0){
                strcat(buffer, ","); //Caso contrário, a posição é um combustível, se não for o primeiro, coloco uma , antes para separar do combustível anterior
            }
            strcat(buffer, v.combustivel[i]); //Concateno o combustível atual no buffer
        }
    }
}

Veiculo* parseVeiculo(char* s){
    Veiculo* veic = (Veiculo*)malloc(sizeof(Veiculo)); //Aloco memória para caber um veiculo
    
    char* token; //Guarda o pedaço do strtok e para cada atributo transformo parte da string total em seu determinado tipo

    token = strtok(s, ",");
    veic->id = atoi(token);

    token = strtok(NULL, ","); //Uso o NULL nas demais para continuar separando até o separador de onde o tok anterior parou
    strcpy(veic->marca, token);

    token = strtok(NULL, ",");
    strcpy(veic->modelo, token);

    token = strtok(NULL, ",");
    veic->ano = atoi(token);

    token = strtok(NULL, ",");
    strcpy(veic->categoria, token);

    token = strtok(NULL, ",");
    parseCombustivel(token, veic); //Chamo o parse para preencher combustivel diretamente

    token = strtok(NULL, ",");
    veic->cilindros = atoi(token);

    token = strtok(NULL, ",");
    veic->cilindrada = atof(token);

    token = strtok(NULL, ",");
    strcpy(veic->transmissao, token);

    token = strtok(NULL, ",");
    strcpy(veic->tracao, token);

    token = strtok(NULL, ",");
    veic->consumoCidade = atof(token);

    token = strtok(NULL, ",");
    veic->consumoEstrada = atof(token);

    token = strtok(NULL, ",");
    veic->co2 = atof(token);

    token = strtok(NULL, ",");
    veic->turbo = (strcmp(token, "true") == 0); 

    token = strtok(NULL, ",");
    veic->dataRegistro = parseData(token); //Chamo o parse para retornar a data preenchida

    return veic;
}

void formatVeiculo(Veiculo v, char* buffer){
    char buffCombustivel[100]; //Crio três buffers temporários para guardar os resultados dos atributos que não consigo colocar diretamente no format
    char buffData[20];
    char buffTurbo[6];

    formatCombustivel(v, buffCombustivel); //Escrevo combustível do jeito que tem que ser com o format e gravo no seu determinado buffer
    formatData(v.dataRegistro, buffData); //Faço o mesmo com a data

    if(v.turbo){
        strcpy(buffTurbo, "true"); 
    }
    else{
        strcpy(buffTurbo, "false");
    }

     sprintf(buffer, "[%d ## %s ## %s ## %d ## %s ## [%s] ## %d ## %.1lf ## %s ## %s ## %.2lf ## %.2lf ## %.1lf ## %s ## %s]",
        v.id, v.marca, v.modelo, v.ano, v.categoria, buffCombustivel, v.cilindros, v.cilindrada,
        v.transmissao, v.tracao, v.consumoCidade, v.consumoEstrada, v.co2,
        buffTurbo, buffData); //Monto o resto do format normalmente
}

Veiculo* lerCsv(char* caminho, int* n){

    FILE* arquivo = fopen(caminho, "r");

    Veiculo* aux = (Veiculo*)malloc(500*sizeof(Veiculo));
    char linhas[500];
    fgets(linhas, sizeof(linhas), arquivo); //Pulo a linha inicial do arquivo
    int i = 0; //Contador para auxiliar na atribuição dos veículos

    while(fgets(linhas, sizeof(linhas), arquivo) != NULL){ //Leio até o fim do arquivo
        Veiculo* tmp = parseVeiculo(linhas); //Chamo o parse para uma variável temporária para que o malloc da função receba o free aqui
        aux[i] = *tmp;
        free(tmp);
        i++; 
    }

    fclose(arquivo); //Fecho o arquivo
    *n = i; //Aqui atribuo o i, que será a quantidade de carros, no n
    return aux;
}

int pegaMaior(Veiculo* a, int b){ //Crio esta função para buscar o maior elemento do vetor veic para montar o vetor de contagem
    int m = 0;

    for(int i=0; i<b; i++){
        if(a[i].cilindros> m){
            m = a[i].cilindros;
        }
    }

    return m;
}


void countingSort(Veiculo* veic, int tamanho, char* result){
    int maior = pegaMaior(veic, tamanho);
    Veiculo* saida = (Veiculo*)malloc(tamanho*sizeof(Veiculo));
    int* contagem = (int*)malloc((maior + 1)*sizeof(int));

    for(int i=0; i<maior+1; i++){ //Inicializo o vetor de contagem com 0
        contagem[i] = 0;
    }

    for(int i=0; i<tamanho; i++){ //Incremento 1 na posição do vetor de contagem referente à quantidade de cilindros de vetor de entrada
        contagem[veic[i].cilindros]++;
    }

    for(int i=1; i<maior+1; i++){ //Torno o vetor de contagem uma soma dos elementos de uma posição atrás
        contagem[i] += contagem[i-1]; 
    }

    for(int i=tamanho-1; i>=0; contagem[veic[i].cilindros]--, i--){ //Neste laço que realizo a operação de ordenação no vetor de saída, percorrendo o vetor de entrada de trás pra frente para manter a estabilidade
        saida[contagem[veic[i].cilindros] - 1] = veic[i];
    }

     for(int c=0; c<tamanho; c++){ //Printo o vetor ordenado, no caso, o vetor de saída
        formatVeiculo(saida[c], result);
        printf("%s\n", result);
    }

    free(saida);
    free(contagem);
}

int main(){
    int id = 0, n, cont = 0;
    char* carro = (char*)malloc(1000); //Crio a string que receberá o format
    Veiculo* carros = (Veiculo*)malloc(100*sizeof(Veiculo));

     //Veiculo* cars = lerCsv("veiculos.csv", &n); //Mando o caminho, um do linux e um do windows, o que eu for usar eu descomento
     Veiculo* cars = lerCsv("/tmp/veiculos.csv", &n);

    while(id != -1){
        scanf("%d", &id); //Leio os ids
            for(int i=0; i<n; i++){
                if(id == cars[i].id){ //Se eu encontrar o id guardo no vetor de veículos adicionados
                   carros[cont] = cars[i];
                   cont++;
                   i = n; //Dou i = n para sair de uma vez da pesquisa
                }
            }
    }

    countingSort(carros, cont, carro); //Chamo o counting que irá ordenar o vetor e printo nela mesma

    free(carro);
    free(cars);
    free(carros); //Dou os últimos free necessários
}