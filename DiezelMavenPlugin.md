Diezel Maven Plugin compiles diezel languages, and implementation located in :
`src/main/diezel/` into `target/generated-sources/diezel/`

To use the plugin just copy paste in the right place.

```
	<build>
		<plugins>
			<plugin>
				<groupId>net.ericaro</groupId>
				<artifactId>diezel-maven-plugin</artifactId>
				<version>1.0.0-beta-1</version>
				<executions>
					<execution>
						<id>diezel</id>
						<goals>
							<goal>diezel</goal>
						</goals>
						<configuration>
						</configuration>
					</execution>
				</executions>
			</plugin>
		</plugins>

	</build>
```


See a fully working [example here](http://code.google.com/p/diezel/source/browse?repo=wiki#git%2Fcardemo).