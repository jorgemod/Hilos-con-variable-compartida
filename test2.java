import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

//cada enanito subira el contador y como son 7 y el for es de 1k 
//al final tendremos un contador de 7k
//si no tuvieramos el metodo synchronized no protegeria nuestra variable y daria cosas distintas
class Contador
{
    private int valor;
    public Contador()
    {
        valor=0;
    }
    public synchronized void incrementar() //sincroniza el valor para los hilos con Synchronized
    { //esto nos ayuda a proteger nuestra variable compartida
        valor ++;

    }
    public int getvalor() //devuelve el valor de la variable
    {
        return valor;
    }
}
class enanito implements Runnable
{
    private String nombre;
    private Contador conta; //variable contador
    public enanito(String nombre,Contador conta) //constructor recibe el nombre y el objeto contador
    {
        this.nombre=nombre; //le damos nombre a nuestro hilo
        this.conta=conta; // inicializamos el objeto compartido
    }
    public void run()
    {
        for(int i=0;i<1000;i++)
        {
           synchronized(conta) //variable compartida
           {
               conta.incrementar(); //incrementa la variable compartida
           }
            
        }
    }
}
class test2
{
    public static void main(String arg[])
{
    //arreglo de enanitos
    String[]nombres={"sabio","gruñon","mudito","dormilon","timido","tontin","bonachon"};
    Contador c= new Contador(); //creamos un nuevo contador unico para todos los hilos
    ExecutorService exec = Executors.newCachedThreadPool(); //recuerdo que es para ejecutar los hilos
    for(String n:nombres) //para cada enanito
    {
        enanito e= new enanito(n,c); //creamos un enanito (Hilo)
        exec.execute(e); //lo ejecutamos es similar al .start
    }
    exec.shutdown();//cuando acaba cerramos la ejecucion de hilos
    try{ //manejo de errores en los hilos
        exec.awaitTermination(100, TimeUnit.SECONDS);
        System.out.println("los siete enanitos han terminado");
    }
    catch(InterruptedException e)
    {
        System.out.println("se interrumpio");
        
    }
    //imprimimos el valor de la variable compartida
    System.out.println("el valor final del contenedor es "+ c.getvalor());
}
}