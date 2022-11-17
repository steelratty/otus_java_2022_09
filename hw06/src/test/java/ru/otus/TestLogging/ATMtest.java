package ru.otus.TestLogging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class ATMtest {
    @Test
    @DisplayName("Проверяем, базовую функционльность")
    void myATMTest() {
        //given
       ATM myATM = new ATMR.Builder()
               .addSell(1000,500)
               .addSell(500,500)
               .addSell(100, 500)
               .build();
       myATM.pushCash(1000,2);
       myATM.pushCash(500,8);
       myATM.pushCash(100,23);

        // выдадим 1500
      Map<Integer,Integer> mr = myATM.pullCash(1500);
      assertThat(mr.get(500)).isEqualTo(1); // выдана 1 штука осталось 7
      assertThat(mr.get(1000)).isEqualTo(1); // выдана 1 штука осталась 1
      assertThat(myATM.get_sum()).isEqualTo(1000*1+500*7+100*23);  // остаток 6800

      //выдадтм 2600
      mr = myATM.pullCash(2600);
      assertThat(mr.get(1000)).isEqualTo(1); // выдана 1 осталось 0
      assertThat(mr.get(500)).isEqualTo(3);  // выдано 3 осталось 4
      assertThat(mr.get(100)).isEqualTo(1);  // выдана 1 осталось 22
      assertThat(myATM.get_sum()).isEqualTo(1000*0+500*4+100*22);  // остаток 6800
    }
}
