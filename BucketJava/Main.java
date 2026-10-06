import java.util.*;
import java.io.*; //Importo para utilizar os comandos necessários para ler arquivos

class Lista{ //Crio a classe lista
    public Veiculo vet[];
    public int n;

    Lista(){
        vet = new Veiculo[1000];
        n = 0;
    }

    void inserirInicio(Veiculo x) throws Exception{ //Faço todos os métodos de inserção e remoção necessários, além do mostrar
        if(n >= vet.length){
            throw new Exception("Erro");
        }

        for(int i=n; i>0; i--){
            vet[i] = vet[i-1];
        }

        vet[0] = x;
        n++;
    }

    void inserirFim(Veiculo x) throws Exception{
        if(n >= vet.length){
            throw new Exception("Erro");
        }

        vet[n] = x;
        n++;
    }

    void inserirPos(Veiculo x, int pos) throws Exception{
        if(n >= vet.length){
            throw new Exception("Erro");
        }

        for(int i=n; i>pos; i--){
            vet[i] = vet[i-1];
        }

        vet[pos] = x;
        n++;
    }

    Veiculo removerInicio()throws Exception{
        if(n == 0){
            throw new Exception("Erro");
        }

        Veiculo resp = vet[0];
        n--;

        for(int i=0; i<n; i++){
            vet[i] = vet[i+1];
        }

        return resp;
    }

    Veiculo removerFim()throws Exception{
        if(n == 0){
            throw new Exception("Erro");
        }

        n--;
        Veiculo resp = vet[n];
        
        return resp;
    }

    Veiculo removerPos(int pos)throws Exception{
        if(n == 0){
            throw new Exception("Erro");
        }

        Veiculo resp = vet[pos];
        n--;

        for(int i=pos; i<n; i++){
            vet[i] = vet[i+1];
        }

        return resp;
    }

    void mostrar(){
        for(int i=0; i<n; i++){
            System.out.println("" + vet[i].format());
        }
        
    }
}

class LeitorCsv{
    public static Veiculo[] ler(String caminho) throws FileNotFoundException{ //Crio a classe do Leitor csv com o comando static ler da classe para ler o arquivo e no fim devolver o array de veículos
        File arquivo = new File(caminho);
        int cont=0;
        int i=0;

        Scanner scCont = new Scanner(arquivo, "UTF-8"); //Crio um scanner para ler o arquivo uma vez e determinar a quantidade de linhas que ele tem, antes eu estava fazendo direto com 500, mas como não funcionaria para qualquer arquivo, decidi mudar

        while(scCont.hasNextLine()){
            scCont.nextLine();
            cont++; //Conto as linhas
        }

        scCont.close(); 
        cont--; //Retiro a linha com as informações que não são necessárias

        Veiculo[] carros = new Veiculo[cont];

        Scanner sc = new Scanner(arquivo, "UTF-8"); //Abro o scanner definitivo

        sc.nextLine(); //Dou essa quebra de linha para pular a linha do arquivo que contém o que são cada uma das informações 

        while(sc.hasNextLine()){
            String linha = sc.nextLine();
            Veiculo car = Veiculo.parseVeiculo(linha); //Até o fim do arquivo leio cada linha como string, mando para o método parse para que um objeto veículo seja retornado
            carros[i] = car; //Adiciono este veículo retornado no vetor de veículos
            i++;
        }
        
        sc.close();
        return carros;  
   }
}

class Data{ //Crio a classe data para trabalhar com objetos da mesma
    private int ano, mes, dia; //Encapsulamento 

    Data(int ano, int mes, int dia){ 
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }

    public int getDia(){ //Métodos gets para retornar o atributo necessário caso eu precise
        return dia;
    }

    public int getMes(){
        return mes;
    }

    public int getAno(){
        return ano;
    }

    public String format(){ //Pego os atributos do objeto e elaboro uma string da forma exata que é pedido
        String aux;
        aux = String.format("%02d/%02d/%04d", dia, mes, ano);
        return aux;
    }

    public static Data parseData(String s){ //Recebo a string da data e transformo em um objeto Data
        String[] separado = s.split("-");

        int[] num = new int[separado.length];
        
        for(int i=0; i<num.length; i++){
            num[i] = Integer.parseInt(separado[i]);
        }

        Data aux = new Data(num[0], num[1], num[2]);
        return aux;
    }
}

 class Veiculo{ //Crio a classe veículo para trabalhar com objetos da mesma
    
    private int id, ano, cilindros;
    private double cilindrada, consumoCidade, consumoEstrada, co2;
    private String marca, modelo, categoria, transmissao, tracao, combustivel[];
    private boolean turbo;
    private Data dataRegistro; //Coloco todos os atributos que estão no enunciado e no arquivo

         Veiculo(int id, String marca, String modelo, int ano, String categoria, String[] combustivel, int cilindros, double cilindrada, 
            String transmissao, String tracao, double consumoCidade, double consumoEstrada, double co2, boolean turbo, Data dataRegistro){ 
            this.id = id;
            this.marca = marca;
            this.modelo = modelo;
            this.ano = ano;
            this.categoria = categoria;
            this.combustivel = combustivel;
            this.cilindros = cilindros;
            this.cilindrada = cilindrada;
            this.transmissao = transmissao;
            this.tracao = tracao;
            this.consumoCidade = consumoCidade;
            this.consumoEstrada = consumoEstrada;
            this.co2 = co2;
            this.turbo = turbo;
            this.dataRegistro = dataRegistro;
        }

    
        public int getId(){ //Métodos gets caso eu precise do valor de um dos atributos
            return id;
        }

        public int getAno(){
            return ano;
        }

        public int getCilindros(){
            return cilindros;
        }

        public double getCilindrada(){
            return cilindrada;
        }

        public double getConsumoCidade(){
            return consumoCidade;
        }

        public double getConsumoEstrada(){
            return consumoEstrada;
        }

        public double getCo2(){
            return co2;
        }

        public String getMarca(){
            return marca;
        }

        public String getModelo(){
            return modelo;
        }

        public String getCategoria(){
            return categoria;
        }

        public String getTransmissao(){
            return transmissao;
        }

        public String getTracao(){
            return tracao;
        }

        public String[] getCombustivel(){
            return combustivel;
        }

        public boolean getTurbo(){
            return turbo;
        }

        public static Veiculo parseVeiculo(String s){ //Método que recebe a string do arquivo e transforma em um objeto de veículo
            String[] dados = s.split(","); //Uso o split para dividir a String em um array de String com o separador ","

            int idAux = Integer.parseInt(dados[0]);
            String marcaAux = dados[1];
            String modeloAux = dados[2];
            int anoAux = Integer.parseInt(dados[3]);
            String categoriaAux = dados[4];
            String[] combustivelAux = dados[5].split(";"); //Divido o combustível em um array de String
            int cilindroAux = Integer.parseInt(dados[6]);
            double cilindradaAux = Double.parseDouble(dados[7]);
            String transmissaoAux = dados[8];
            String tracaoAux = dados[9];
            double consumoCidadeAux = Double.parseDouble(dados[10]);
            double consumoEstradaAux = Double.parseDouble(dados[11]);
            double co2Aux = Double.parseDouble(dados[12]);
            boolean turboAux = Boolean.parseBoolean(dados[13]);
            Data dataAux = Data.parseData(dados[14]);

            Veiculo carro = new Veiculo(idAux, marcaAux, modeloAux, anoAux, categoriaAux, combustivelAux, cilindroAux, cilindradaAux, transmissaoAux, tracaoAux, consumoCidadeAux, consumoEstradaAux, co2Aux, turboAux, dataAux);

            return carro;
        }

        public String combustivelFormat(){ //Crio um format específico para o combustível pois o mesmo é um array de String e precisa aparecer na tela de uma forma específica
            String aux = "";
                for(int i=0; i<combustivel.length; i++){
                    aux += combustivel[i];
                    if(i < combustivel.length - 1){
                         aux += ",";
                    }
                }
            return aux;
            
        }

        public String format(){ //Format do veiculo para aparecer na tela do jeito que é requisitado
            String aux;
            aux = String.format(Locale.US, "[%d ## %s ## %s ## %d ## %s ## [%s] ## %d ## %s ## %s ## %s ## %.2f ## %.2f ## %s ## %b ## %s]", id, marca, modelo, ano, categoria, combustivelFormat(), cilindros, cilindrada, transmissao, tracao, consumoCidade, consumoEstrada, co2, turbo, dataRegistro.format());
            return aux; //Uso o Locale.US para os números reais utilizarem "." em vez de "," como separador
        }

}

public class Main{
    
    public static void bucketSort(Veiculo[] v, int tamanho){
        
        Lista[] baldes = new Lista[10]; //São 10 baldes conforme o enunciado
        for(int j=0; j<10; j++){
            baldes[j] = new Lista(); //Instancio uma lista para cada balde, pois não sei quantos elementos terão em cada balde
        }

        for(int i=0; i<tamanho; i++){ //Neste for divido os veiculos de v em cada balde com as operações necessárias
            double c = v[i].getCilindrada();
            c /= 8.1;
            c *= 10;
            int a = (int)c;
            try{
                baldes[a].inserirFim(v[i]); //Tento inserir por causa da exceção lançada no método inserirFim
            }
            
            catch(Exception e){
                System.out.println("Nao foi possível inserir o elemento no balde");
            }
        }

        for(int b=0; b<10; b++){ //Neste segundo for eu ordeno por inserção os elementos de cada balde com base na cilindrada de cada um
            for(int i = 0; i<baldes[b].n; i++){
                Veiculo tmp = baldes[b].vet[i];
                int j = i-1;
                while(j>=0 && baldes[b].vet[j].getCilindrada() > tmp.getCilindrada()){
                    baldes[b].vet[j+1] = baldes[b].vet[j];
                    j--;
                }
            
            baldes[b].vet[j+1] = tmp;
            }   
        }

        for(int b=0; b<10; b++){ //Neste terceiro for eu printo todos os veículos de cada balde já ordenados por cilindrada
            for(int i=0; i<baldes[b].n; i++){
                System.out.println("" + baldes[b].vet[i].format());
            }
        }
}
    
    public static void main(String[] args){
        int id = 0, cont = 0;
        Veiculo[] aux = new Veiculo[100];
        Scanner sc = new Scanner(System.in);
        try{ //Tento ler o arquivo
            //Veiculo[] cars = LeitorCsv.ler("veiculos.csv"); //Windows
            Veiculo[] cars = LeitorCsv.ler("/tmp/veiculos.csv"); //Linux //Um caminho de arquivo para o linux/verde e o outro pra minha máquina

            while(id != -1){ //Leio os ids até -1
                id = sc.nextInt();

                for(int j = 0; j < cars.length; j++){

                    if(id == cars[j].getId()){ //Se o id bater com o de algum carro do array formato ele e printo
                        aux[cont] = cars[j];
                        cont++; //Contador para contar quantos veiculos aux armazenará
                        j = cars.length; //Se achar dou j=cars.length para sair imediatamente do for e pular para o próximo
                    }
                }
            }

            bucketSort(aux, cont); //Ordeno o array com o BucketSort e printo o resultado
        }

        catch(FileNotFoundException e){ //Se não encontrar no arquivo cai na exceção
            System.out.println("Arquivo nao encontrado");
        }
        
        sc.close();
    }

}