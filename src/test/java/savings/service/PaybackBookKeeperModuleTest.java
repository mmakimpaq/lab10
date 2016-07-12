package savings.service;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.Assertions.assertThat;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.time.DateTime.now;
import static savings.PaybackFixture.creditCardNumber;
import static savings.PaybackFixture.merchantNumber;
import static savings.PaybackFixture.purchase;

import javax.sql.DataSource;

import org.joda.money.Money;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import common.db.LocalDatabaseConfiguration;
import common.sql.TestDataSourceFactory;
import savings.model.PaybackConfirmation;
import savings.model.Purchase;
import savings.repository.impl.RepositoryConfiguration;
import savings.service.PaybackBookKeeper;
import savings.service.impl.ServiceConfiguration;

@Ignore // TODO #1 : remove after completion of all other tasks
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    LocalDatabaseConfiguration.class, RepositoryConfiguration.class, ServiceConfiguration.class
})
@Transactional
public class PaybackBookKeeperModuleTest {

    @Autowired
    PaybackBookKeeper bookKeeper;

    @Test
    public void shouldThrowWhenAccountNotFound() {
        Purchase purchase = new Purchase(Money.of(EUR, 100L), "4321432143214321", merchantNumber, now());

        catchException(bookKeeper, EmptyResultDataAccessException.class).registerPaybackFor(purchase);

        assertThat(caughtException()).isNotNull();
    }

    @Test
    public void shouldThrowWhenMerchantNotFound() {
        Purchase purchase = new Purchase(Money.of(EUR, 100L), creditCardNumber, "1111111111", now());

        catchException(bookKeeper, EmptyResultDataAccessException.class).registerPaybackFor(purchase);

        assertThat(caughtException()).isNotNull();
    }

    @Test
    public void shouldRegisterPayback() {
        PaybackConfirmation confirmation = bookKeeper.registerPaybackFor(purchase());

        assertThat(confirmation.getNumber()).isNotNull();
        assertThat(confirmation.getIncome().getAmount()).isEqualTo(Money.of(EUR, 6L));
        assertThat(confirmation.getIncome().getDistributions()).hasSize(2);
        assertThat(confirmation.getIncome().getDistribution("Glock").getAmount()).isEqualTo(Money.of(EUR, 3L));
        assertThat(confirmation.getIncome().getDistribution("M60").getAmount()).isEqualTo(Money.of(EUR, 3L));
    }
}
