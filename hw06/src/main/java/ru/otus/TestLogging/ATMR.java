package ru.otus.TestLogging;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ATMR implements ATM{
    private Map<Integer,CellATM> cells = new TreeMap<> (Collections.reverseOrder());

    private ATMR(Builder builder){
        cells.putAll(builder.cellsb);
    };
    public static class Builder {
        private Map<Integer,CellATMR> cellsb = new TreeMap<> (Collections.reverseOrder());
        Builder addSell( int nominal, int maxN ){
            cellsb.put( nominal, new CellATMR( nominal, maxN ) );
            return this;
        };
        ATMR build(){
            return new ATMR(this);
        }

    }

    @Override
    public Map<Integer, Integer> pullCash(int amount ) {
        // прогоняем по коллекции (она отсортирована на уменьшение номинала),
        // проверим, хватает ли места для размещения денег
        Map<Integer, Integer> outmap  = new HashMap<>();
        int tmpAmount = amount;
        for (Integer i : cells.keySet()) {
            CellATM cell = cells.get(i);
             // выдать все что можем, из ячейки
            if (i <= tmpAmount && cell.getN() > 0) {
                if (tmpAmount / i <= cell.getN()) {
                    tmpAmount = tmpAmount - (tmpAmount / i) * i;
                } else {
                    // выдаем, что есть
                    tmpAmount = tmpAmount - cell.getN() * i;
                }
            }
        }
        if ( tmpAmount > 0 ) {
            throw new IllegalArgumentException(" ATM can't pull cash "+ tmpAmount+ " of amount");
        }
        // проверка на возможность выдачи прошла, выдаем из ячеек
        for (Integer i : cells.keySet()) {
            CellATM cell = cells.get(i);
            // выдать все что можем, из ячейки
            if (i <= amount && cell.getN() > 0) {
                if (amount / i <= cell.getN()) {
                    cell.pull( amount/i );
                    outmap.put(i, amount/i);
                    amount = amount - amount / i * i;

                } else {
                    // выдаем, что есть
                    outmap.put(i, cell.getN());
                    cell.pull(cell.getN());
                    amount = amount- outmap.get(i) * i;
                }
            }
        }
        return outmap; // коллекция с купюрами и их номиналом
    }

    @Override
    public int get_sum() {
        int tmp_sum =0;
        for (Integer i : cells.keySet()) {
            tmp_sum += cells.get(i).getN() * i;
        }
        return tmp_sum;
    }

    @Override
    public void pushCash(int  nominal, int n ) {
        CellATM cell = cells.get(nominal);
        if (cell != null && (cell.getMaxN() - cell.getN() - n) >= 0  ) {
            cell.push(n);
        }
        else {
            throw new IllegalArgumentException(" I can not pull all cash, free space for "+ nominal +"nominal - " + String.valueOf(cell.getMaxN() - cell.getN() - n) );
        }
    }

}
