package ru.otus.ATM;

public class CellATMR implements CellATM{
    // номинал банкнот
    private final int nominal;
    // емкость бункера
    private final int maxN;
    // текущее количество банкнот
    private int n;

    public CellATMR(int nominal, int maxN) {
        this.nominal = nominal;
        this.maxN = maxN;
    }


    @Override
    public void push(int n) {
       // если вносимое число > емкости ячейки, то ошибка
        if (n < 0) {
            throw new IllegalArgumentException ("May be only >= 0");
        }

        if (this.n + n > maxN ) {
           throw new IllegalArgumentException("Overload sell, "
                   +" nominal = " + String.valueOf(nominal)
                   +" maxN = "    + String.valueOf(maxN)
                   +" new n = "   + String.valueOf(this.n + n) );
       }
        this.n += n;
    }

    @Override
    public void pull(int n) {
        if (n < 0) {
            throw new IllegalArgumentException ("n may be only >= 0");
        }
        if (this.n - n < 0 ) {
            throw new IllegalArgumentException(" n is greater than the cell balance"
                    +" nominal = " + String.valueOf(nominal)
                    +" maxN = "    + String.valueOf(maxN)
                    +" new n = "   + String.valueOf(this.n - n) );
        }
        this.n = this.n - n;
    }

    @Override
    public int getN() {
        return n;
    }

    @Override
    public int getMaxN() {
        return maxN;
    }

    @Override
    public int getNominal() {
        return nominal;
    }
}
