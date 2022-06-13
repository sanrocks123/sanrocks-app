<h3>Bux Trading Bot</h3>

<p>Notes</p>
<ol>
    <li>jdk 11 recommended </li>
    <li>set console system environment property <code>[export PROFILE= localhost|dev]</code>. Default profile is set to <code>"dev"</code></li>
    <li>for localhost profile - ensure localhost bux server is up & running (<code>java -jar docs/bux-server.jar</code>)</li>
    <li>default logging level is set to INFO. To change app logging level <code>export LOG_LEVEL=[INFO|DEBUG|TRACE]</code></li>
    <li>app entry point class <code>bux.tradingbot.main.BuxTradingBotServiceSelfHostApp</code></li>
    <li>it takes a while before position open/close conditions are satisfied, so trade logs may take a while to appear on console</li>
</ol>

<p>Build & Run Steps</p>
<ol>
    <li>switch to project root directory <code>/bux-trade-bot</code></li>
    <li><code>export LOG_LEVEL="INFO"</code></li>
    <li>execute command </li>
        <ol>
            <li>on windows console -> <code>gradlew clean build run</code></li>
            <li>on linux console -> <code>./gradlew clean build run</code></li>
        </ol>
    <li> view swagger api docs - <a href="http://localhost:1000/swagger-ui/">http://localhost:1000/swagger-ui/</a></li>
</ol>

<p>Todos</p>
<ol>
    <li>update docker build script to use tar compression</li>
    <li>gradle file cleanup</li>
    <li>implement reactive APIs using webflux library</li>
</ol>