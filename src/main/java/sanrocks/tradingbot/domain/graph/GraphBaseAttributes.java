package sanrocks.tradingbot.domain.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphBaseAttributes {

    private Extension extension = new Extension();
}
