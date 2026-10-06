import java.util.*;
import java.io.*; //Importo para utilizar os comandos necessários para ler arquivos

class CelulaDupla{ //Crio a classe da célula
    public Veiculo elemento;
    public CelulaDupla prox, ant;

    CelulaDupla(Veiculo x){
        this.elemento = x;
        this.prox = null;
        this.ant = null;
    }
}

class ListaDupla{ //Crio a classe lista dupla
    public CelulaDupla primeiro, ultimo;
    public int n;

    ListaDupla(){
        primeiro = new CelulaDupla(null);
        ultimo = primeiro;
        n = 0;
    }

    void inserirInicio(Veiculo x) throws Exception{ //Faço todos os métodos de inserção e remoção necessários, além do mostrar
        CelulaDupla tmp = new CelulaDupla(x);
        tmp.prox = primeiro.prox;
        tmp.ant = primeiro;
        primeiro.prox=tmp;

        if(primeiro == ultimo){
            ultimo = tmp;
        }

        else{
            tmp.prox.ant = tmp;
        }

        n++;
    }

    void inserirFim(Veiculo x) throws Exception{
       ultimo.prox = new CelulaDupla(x);
       ultimo.prox.ant = ultimo;
       ultimo = ultimo.prox;
       n++;
    }

    void inserirPos(Veiculo x, int pos) throws Exception{
        if(pos < 0 || pos > n){
            throw new Exception("Erro");
        }

        else if(pos == 0){
            inserirInicio(x);
        }

        else if(pos == n){
            inserirFim(x);
        }

        else{
            CelulaDupla i = primeiro;
            for(int j=0; j<pos; j++, i = i.prox);

            CelulaDupla tmp = new CelulaDupla(x);
            tmp.prox = i.prox;
            tmp.ant = i;
            tmp.prox.ant = tmp.ant.prox = tmp;
            n++;
        }
    }

    Veiculo removerInicio()throws Exception{
       if(primeiro == ultimo){
        throw new Exception("Erro");
       }

       primeiro = primeiro.prox;
       Veiculo resp = primeiro.elemento;
       primeiro.ant = null;

       n--;
       return resp;
    }

    Veiculo removerFim()throws Exception{
        if(primeiro == ultimo){
            throw new Exception("Erro");
        }

        Veiculo resp = ultimo.elemento;
        ultimo = ultimo.ant;
        ultimo.prox.ant = null;
        ultimo.prox = null;

        n--;
        return resp;

    }

    Veiculo removerPos(int pos)throws Exception{

       if(primeiro == ultimo || pos < 0 || pos > n){
        throw new Exception("Erro");
       }

       else if(pos == 0){
         return removerInicio();
       }

       else if(pos == n-1){
         return removerFim();
       }

       else{
        CelulaDupla i = primeiro;
        for(int j=0; j<=pos; j++, i = i.prox);
        Veiculo resp = i.elemento;
        i.ant.prox = i.prox;
        i.prox.ant = i.ant;

        n--;
        return resp;
       }
    }

    void mostrar(){
       CelulaDupla i = primeiro.prox;
       while(i != null){
        System.out.println("" + i.elemento.format());
        i = i.prox;
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
    public static void main(String[] args){
        int id = 0, quantOp, pos; //Crio uma variável para ler a quantidade de operações e uma para a posição que será removido ou inserido um veiculo
        ListaDupla listaDupla = new ListaDupla(); //Crio a lista dupla flexível
        String operacao = "";
        Scanner sc = new Scanner(System.in);
        try{ //Tento ler o arquivo
          //  Veiculo[] cars = LeitorCsv.ler("veiculos.csv"); //Windows
            Veiculo[] cars = LeitorCsv.ler("/tmp/veiculos.csv"); //Linux //Um caminho de arquivo para o linux/verde e o outro pra minha máquina

            while(id != -1){ //Leio os ids até -1
                id = sc.nextInt();

                for(int j = 0; j < cars.length; j++){

                    if(id == cars[j].getId()){ //Se o id bater com o de algum carro do array insiro ele na lista
                        listaDupla.inserirFim(cars[j]);
                        j = cars.length; //Se achar dou j=cars.length para sair imediatamente do for e pular para o próximo
                    }
                }
            }

            quantOp = sc.nextInt(); //Leio o número de operações

            for(int a=0; a<quantOp; a++){
                operacao = sc.next(); //Leio a string assim para evitar o bug do scanner de int antes do line

                if(operacao.equals("II")){ //Se digitarem II busco o veículo e insiro no início
                    id = sc.nextInt();
                    for(int i=0; i<cars.length; i++){
                        if(id == cars[i].getId()){
                            listaDupla.inserirInicio(cars[i]);
                            i = cars.length;
                        }
                    }
                }

                else if(operacao.equals("IF")){ //Se digitarem IF busco o veículo e insiro no fim
                    id = sc.nextInt();
                    for(int i=0; i<cars.length; i++){
                        if(id == cars[i].getId()){
                            listaDupla.inserirFim(cars[i]);
                            i = cars.length;
                        }
                    }
                }

                else if(operacao.equals("I*")){ //Se digitarem I* busco o veículo e insiro na posição
                   pos = sc.nextInt();
                   id = sc.nextInt();
                   
                   for(int i=0; i<cars.length; i++){
                        if(id == cars[i].getId()){
                            listaDupla.inserirPos(cars[i], pos);
                            i = cars.length;
                        }
                    }
                }

                else if(operacao.equals("RI")){ //Se digitarem RI removo do início
                    Veiculo resp = listaDupla.removerInicio();
                    System.out.println("(R)" + resp.getMarca() + " " + resp.getModelo());
                }

                else if(operacao.equals("RF")){ //Se digitarem RF removo no fim
                   Veiculo resp = listaDupla.removerFim();
                   System.out.println("(R)" + resp.getMarca() + " " + resp.getModelo());
                }

                else if(operacao.equals("R*")){ //Se digitarem R* removo na posição
                    pos = sc.nextInt();
                    Veiculo resp = listaDupla.removerPos(pos);
                    System.out.println("(R)" + resp.getMarca() + " " + resp.getModelo());
                }

                else{ //Se não for nenhuma das opções acima a operação não existe
                    System.out.println("Nao existe essa operacao");
                }
            }

            listaDupla.mostrar(); //Printo a lista final

        }

        catch(FileNotFoundException e){ //Se não encontrar no arquivo cai na exceção
            System.out.println("Arquivo nao encontrado");
        }

        catch(Exception e){ //Se tentar inserir com a lista cheia ou remover algo em uma lista vazia, ou se enviar uma posição inválida
            System.out.println("Não foi possível inserir/remover");
        }
        
        sc.close();
    }

}