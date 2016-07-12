package savings.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.jadira.usertype.dateandtime.joda.PersistentDateTime;
import org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount;
import org.joda.money.Money;
import org.joda.time.DateTime;

import common.hibernate.PersistentPercentage;
import common.math.Percentage;

@Entity
@TypeDefs({
        @TypeDef(name = "percentage", defaultForType = Percentage.class, typeClass = PersistentPercentage.class),
        @TypeDef(name = "dateTime", defaultForType = DateTime.class, typeClass = PersistentDateTime.class),
        @TypeDef(name = "money", defaultForType = Money.class, typeClass = PersistentMoneyAmount.class, parameters = {
                @Parameter(name = "currencyCode", value = "EUR")
        })
})
public class BecauseJpaAndStuff {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_payback_confirmation_number")
    @SequenceGenerator(name = "seq_payback_confirmation_number", sequenceName = "SEQ_PAYBACK_CONFIRMATION_NUMBER")
    private Long number;

    public Long getNumber() {
        return number;
    }
}
