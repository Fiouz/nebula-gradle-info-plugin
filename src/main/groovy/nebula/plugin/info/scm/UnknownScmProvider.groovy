/*
 * Copyright 2014-2019 Netflix, Inc.
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

package nebula.plugin.info.scm

import org.gradle.api.Project
import org.gradle.api.provider.ProviderFactory

class UnknownScmProvider extends AbstractScmProvider {

    public static final String LOCAL = 'LOCAL'

    UnknownScmProvider(ProviderFactory providerFactory) {
        super(providerFactory)
    }

    @Override
    boolean supports(Project project) {
        return true
    }

    @Override
    String calculateModuleOrigin(File projectDir) {
        return LOCAL
    }

    @Override
    String calculateModuleSource(File projectDir) {
        return projectDir.absolutePath
    }

    @Override
    String calculateChange(File projectDir) {
        return null
    }

    @Override
    String calculateBranch(File projectDir) {
        return null
    }
}
