package reserva.varios;


public class Fecha {
    private int día;
    private int mes;
    private int año;
    

    /**
     * Método constructor que crea un objeto fecha con atributos: día, mes y año
     * que se introducen como parámetros
     *
     * @param día
     * @param mes
     * @param año
     */
    public Fecha(int día, int mes, int año) {
        if (mes>=1&&mes<=12) {
            this.mes = mes;
        }
        if (día>=1&&día<=this.DíasMes()) {
            this.día = día;
        }
        if (año>0) {
            this.año = año;
        }  
        if(!(mes>=1&&mes<=12)||!(día>=1&&día<=this.DíasMes())||!(año>0)){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Este método retorna true si el año del objeto es bisiesto y false en el
     * caso contrario
     *
     * @return
     */
    public boolean AñoBisiesto() {
        if (this.año % 4 == 0) {
            if (this.año % 100 == 0) {
                return this.año % 400 == 0;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Este método retorna la cantidad de días que tiene el mes del objeto
     *
     * @return
     */
    public int DíasMes() {
        if (this.mes == 2) {
            if (this.AñoBisiesto()) {
                return 29;
            } else {
                return 28;
            }
        } else {
            if (this.mes == 4 || this.mes == 6 || this.mes == 9 || this.mes == 11) {
                return 30;
            } else {
                return 31;
            }
        }
    }

    /**
     * Este método incrementa un día a un objeto tipo fecha
     *
     */
    public void IncrementaDía() {
        if (this.día == this.DíasMes()) {
            this.día = 1;
            this.mes++;
            if (this.mes == 13) {
                this.mes = 1;
                this.año++;
            }
        } else {
            this.día++;
        }
    }

    /**
     * Este método incrementa n días a un objeto tipo fecha
     *
     * @param n
     */
    public void IncrementaDía(int n) {
        int cont;
        cont = 1;
        while (cont <= n) {
            if (this.día == this.DíasMes()) {
                this.día = 1;
                this.mes++;
                if (this.mes == 13) {
                    this.mes = 1;
                    this.año++;
                }
            } else {
                this.día++;
            }
            cont++;
        }
    }

    /**
     * Este método decrementa un día a un objeto tipo fecha
     *
     */
    public void DecrementaDía() {
        if (this.día == 1) {
            this.mes--;
            if (this.mes == 0) {
                this.mes = 12;
                this.año--;
            }
            this.día = this.DíasMes();
        } else {
            this.día--;
        }
    }

    /**
     * Este método decrementa n días a un objeto tipo fecha
     *
     * @param n
     */
    public void DecrementaDía(int n) {
        int cont;
        cont = 1;
        while (cont <= n) {
            if (this.día == 1) {
                this.mes--;
                if (this.mes == 0) {
                    this.mes = 12;
                    this.año--;
                }
                this.día = this.DíasMes();
            } else {
                this.día--;
            }
            cont++;
        }
    }

    public String Imprimir() {
        return this.día + "/" + this.mes + "/" + this.año;
    }
    public String Imprimir(boolean tipo){
        if (tipo==true) {
            return Imprimir();
        }
        else{
            
            return ""+(this.día+"/"+this.mes+"/"+this.año);
        }
    }
    /**
     * Este método compara la fecha del objeto con la ingresada como parámetro y
     * retorna 1 en caso de que la primer fecha sea mayor que la segunda, 0 en
     * caso de ser iguales y -1 en caso de que la primer fecha sea menor que la
     * segunda
     *
     * @param día
     * @param mes
     * @param año
     * @return
     */
    public int Comparar(int día, int mes, int año) {
        if (this.año > año) {
            return 1;
        } else {
            if (this.año < año) {
                return -1;
            } else {
                if (this.mes > mes) {
                    return 1;
                } else {
                    if (this.mes < mes) {
                        return -1;
                    } else {
                        if (this.día > día) {
                            return 1;
                        } else {
                            if (this.día < día) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * Este método compara la fecha del objeto con la actual y
     * retorna 1 en caso de que la primer fecha sea mayor que la segunda, 0 en
     * caso de ser iguales y -1 en caso de que la primer fecha sea menor que la
     * segunda
     * @return 
     */
    public int Comparar(Fecha f){
        return Comparar(f.día,f.mes,f.año);
    }
    public int getDía(){
        return this.día;
    }
    public int getMes(){
        return this.mes;
    }
    public int getAño(){
        return this.año;
    }
    public void setFecha(int día2, int mes2, int año2){
        if (mes>=1&&mes<=12) {
            this.mes = mes2;
        }    
        if (día>=1&&día<=this.DíasMes()) {
            this.día = día2;
        }
        if (año>0) {
            this.año = año2;
        }   
    }
    /*
    public String Edad(int día, int mes, int año){
        Fecha aux;
        aux=new Fecha();
        aux.setFecha(día, mes-1, año);
        int días, meses, años;
        años =año -this.getAño();
       if (mes >=this.mes){
       meses =mes -this.getMes();
       }
       else
       {
       meses =mes + (12 -this.getMes());
       años = años - 1;
       }
       if (día >=this.getDía()){
       días =día -this.getDía();
       }
       else
       {
       días =día + (aux.DíasMes() -this.getDía());
       meses = meses - 1;
       }      
        return días+" días, "+meses+" meses, "+años+" años";
    }
    public String Edad(Fecha f){
        return Edad(f.getDía(),f.getMes(),f.getAño());
    }
    public String Edad(){
        return Edad(date.getDayOfMonth(),date.getMonthValue(),date.getYear());
    }*/
}
