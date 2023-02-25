/* (C) 2023 */
package sanrocks.tradingbot.service;

import com.apollographql.apollo3.ApolloClient;
import com.apollographql.apollo3.api.Optional;
import com.sanrocks.graphql.CompanyByIdQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ApolloGraphQlTest {

    @Test
    public void testApolloClient() {

        ApolloClient apolloClient =
                new ApolloClient.Builder()
                        .serverUrl("http://localhost:1000/trading-bot/graphql")
                        .build();

        apolloClient.query(new CompanyByIdQuery(Optional.present("83")));
    }
}
