/*
 * Copyright 2016 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nebula.plugin.info

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

class InfoPluginIntegrationSpec extends IntegrationSpec {
    def 'it returns build reports at the end of the build'() {
        given:
        buildFile << """
            ${applyPlugin(InfoPlugin)}
            apply plugin: 'java'
            
            repositories { jcenter() }
            dependencies {
                compile 'com.google.guava:guava:18.0'
            }
            def broker = project.plugins.getPlugin(${InfoBrokerPlugin.name})

            gradle.buildFinished {
                println broker.buildReports().get('resolved-dependencies')
            }
        """.stripIndent()

        this.writeHelloWorld('com.nebula.test')

        when:
        ExecutionResult result = runTasksSuccessfully('assemble')

        then:
        result.standardOutput.contains('-dependencies={Resolved-Dependencies-CompileClasspath=com.google.guava:guava:18.0}}')
    }

    def 'it returns build reports at the end of multiproject build'() {
        given:
        buildFile << """
            allprojects {
                ${applyPlugin(InfoPlugin)}
            }

            subprojects {
                repositories { jcenter() }
            }

            def broker = project.plugins.getPlugin(${InfoBrokerPlugin.name})

            gradle.buildFinished {
                println broker.buildReports().get('resolved-dependencies')
            }
        """.stripIndent()
        def common = addSubproject('common', '''\
            apply plugin: 'java'
            dependencies {
                compile 'com.google.guava:guava:18.0'
            }
            '''.stripIndent())
        writeHelloWorld('nebula.common', common)
        def app = addSubproject('app', '''\
            apply plugin: 'java'
            dependencies {
                compile 'com.google.guava:guava:19.0'
            }
            '''.stripIndent())
        writeHelloWorld('nebula.app', app)

        when:
        ExecutionResult result = runTasksSuccessfully('build')

        then:
        result.standardOutput.contains('common-dependencies={Resolved-Dependencies-CompileClasspath=com.google.guava:guava:18.0}')
        result.standardOutput.contains('app-dependencies={Resolved-Dependencies-CompileClasspath=com.google.guava:guava:19.0}')
    }
}
