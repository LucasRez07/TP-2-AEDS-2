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

typedef struct Celula{ //Crio a struct célula
    Veiculo elemento;
    struct Celula* prox;
}Celula;

Celula* novaCel(Veiculo x){ //Crio a função que faz a função do new do java
    Celula* nova = (Celula*)malloc(sizeof(Celula));
    nova->elemento = x;
    nova->prox = NULL;
    return nova;
}

typedef struct Lista{ //Crio a struct lista
    Celula* primeiro;
    Celula* ultimo;
}Lista;

void start(Lista* l){ //Como se fosse o construtor da lista
    Veiculo aux;
    l->primeiro = novaCel(aux);
    l->ultimo = l->primeiro;
}

void inserirInicio(Lista* list, Veiculo* c, int Id, int tam){ //Crio a função que insere no início

    Celula* tmp = NULL;

    for(int i=0; i<tam; i++){

        if(Id == c[i].id){

            tmp = novaCel(c[i]);
            i = tam;    

        }
    }

    tmp->prox = list->primeiro->prox;
    list->primeiro->prox = tmp;

    if(list->primeiro == list->ultimo){
        list->ultimo = tmp;
    }
   
}

void inserirFim(Lista* list, Veiculo* c, int Id, int tam){ //Crio a função que insere no fim

    for(int i=0; i<tam; i++){

        if(Id == c[i].id){

            list->ultimo->prox = novaCel(c[i]);
            i = tam;  

        }
    }

    list->ultimo = list->ultimo->prox;
   
}

void inserirPos(Lista* list, Veiculo* c, int Id, int tam, int pos){ //Crio a função que insere na posição

    if(pos < 0 || pos > tam){ //Cuido das exceções
        exit(1);
    }

    else if(pos == 0){ //Chamo o inserir início se a posição for 0
        inserirInicio(list, c, Id, tam);
    }

    else if(pos == tam){ //Chamo o inserir no fim se a posição for a final
        inserirFim(list, c, Id, tam);
    }

    else{
        Celula* tmp = NULL;
        Celula* i = list->primeiro;

        for(int j=0; j<pos; j++, i = i->prox);

        for(int k=0; k<tam; k++){

            if(Id == c[k].id){

                tmp = novaCel(c[k]);
                k = tam;    
            }
        }

        tmp->prox = i->prox;
        i->prox = tmp;
    }

}

Veiculo removerInicio(Lista* list){ //Crio a função que remove no início

    if(list->primeiro == list->ultimo){ //Cuido da exceção caso a lista esteja vazia
        exit(1);
    }

    Celula* i = list->primeiro;
    list->primeiro = list->primeiro->prox;
    Veiculo x = list->primeiro->elemento;

    free(i);

    return x;

}

Veiculo removerFim(Lista* list){ //Crio a função que remove no fim

    if(list->primeiro == list->ultimo){ //Cuido da exceção caso a lista esteja vazia
        exit(1);
    }

    Celula* i;

    for(i = list->primeiro; i->prox != list->ultimo; i = i->prox); 

    Veiculo x = list->ultimo->elemento;
    list->ultimo = i;
    
    free(list->ultimo->prox);
    list->ultimo->prox = NULL;

    return x;

}

Veiculo removerPos(Lista* list, int pos, int tam){//Crio a função que remove na posição

    Veiculo x;

    if(list->primeiro == list->ultimo || pos < 0 || pos >= tam){ //Cuido das possíveis exceções
        exit(1);
    }

    else if(pos == 0){ //Chamo o remover no início caso a posição seja 0
        x = removerInicio(list);
    }

    else if(pos == (tam - 1)){ //Chamo o remover no fim caso a posição seja tamanho - 1
        x = removerFim(list);
    }

    else{

        Celula* i = list->primeiro;

        for(int j=0; j<pos; j++, i = i->prox);

        Celula* tmp = i->prox;
        i->prox = tmp->prox;

        x = tmp->elemento;
        
        free(tmp);

    }

    return x;

}

void mostrar(Lista* list){ //Função para printar a lista 
    Celula* i = list->primeiro->prox;
    char* resultado = (char*)malloc(1000*sizeof(char));

    while(i != NULL){
        formatVeiculo(i->elemento, resultado);
        printf("%s\n", resultado);
        i = i->prox;
    }

    free(resultado);

}

int main(){
    int id = 0, n, quantOp, pos; //Crio as variáveis necessárias
    char operacao[3];
    Lista veics; //Crio a lista
    
    start(&veics); //Instancio a lista

    //Veiculo* cars = lerCsv("veiculos.csv", &n); //Mando o caminho, um do linux e um do windows, o que eu for usar eu descomento
    Veiculo* cars = lerCsv("/tmp/veiculos.csv", &n);

    while(id != -1){
        scanf("%d", &id); //Leio os ids
            for(int i=0; i<n; i++){
                if(id == cars[i].id){ //Se eu encontrar o id mando inserir no fim da lista
                    inserirFim(&veics, cars, id, n);
                    i = n; //Dou i = n para sair de uma vez da pesquisa
                }
            }
    }

    scanf("%d", &quantOp); //Leio quantas operações serão realizadas

    for(int a=0; a<quantOp; a++){

        scanf(" %s", operacao); //Leio qual operação será realizada

        if(strcmp(operacao, "II") == 0){ //Se for II leio o id e insiro no início

            scanf("%d", &id);
            inserirInicio(&veics, cars, id, n);

        }

        else if(strcmp(operacao, "IF") == 0){ //Se for IF leio o id e insiro no fim

            scanf("%d", &id);
            inserirFim(&veics, cars, id, n);

        }

        else if(strcmp(operacao, "I*") == 0){ //Se for I* leio a posição, o id e insiro na posição

            scanf("%d%d", &pos, &id);
            inserirPos(&veics, cars, id, n, pos);

        }

        else if(strcmp(operacao, "RI") == 0){ //Se for RI removo do início e printo

            Veiculo resp = removerInicio(&veics);
            printf("(R)%s %s\n", resp.marca, resp.modelo);

        }

        else if(strcmp(operacao, "RF") == 0){ //Se for RF removo do fim e printo

            Veiculo resp = removerFim(&veics);
            printf("(R)%s %s\n", resp.marca, resp.modelo);

        }

        else if(strcmp(operacao, "R*") == 0){ //Se for R* leio a posição, removo e printo

            scanf("%d", &pos);
            Veiculo resp = removerPos(&veics, pos, n);
            printf("(R)%s %s\n", resp.marca, resp.modelo);

        }

        else{ //Caso contrário não existe essa operação
            printf("Nao existe essa operacao\n");
        }
 
    }

     mostrar(&veics);
 
    free(cars);
}