package org.apache.maven.plugin.dependency.tree;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.execution.RuntimeInformation;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Displays the dependency tree for this project by collecting (not resolving) the dependencies.
 * This goal requires Maven 3.0 or higher to function because it uses "requiresDependencyCollection".
 * This means that it lists the groupId:artifactId:version information by downloading the pom files
 * without downloading the actual artifacts such as jar files.
 * This is very useful when full dependency resolution might fail due to projects which haven't been built yet.
 * <p/>
 * It is identical to {@link TreeMojo} except for using the requiresDependencyCollection annotation
 * attribute instead of requiresDependencyResolution.
 *
 * @author <a href="mailto:epabst@gmail.com">Eric Pabst</a>
 * @version $Id$
 * @since 2.9
 */
@Mojo( name = "collect-tree", requiresDependencyCollection = ResolutionScope.TEST, threadSafe = true )
public class CollectTreeMojo extends TreeMojo
{
    /**
     * The runtime information for Maven, used to retrieve Maven's version number.
     *
     * @component
     */
    @Component
    private RuntimeInformation runtimeInformation;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if ( !isSkip() && runtimeInformation.getApplicationVersion().compareTo(new DefaultArtifactVersion("3.0")) < 0 ) {
            throw new UnsupportedOperationException(
                    "dependency:collect-tree requires Maven 3.0 or higher because it uses 'requiresDependencyCollection'. "
                            + " Use dependency:tree instead.");
        }
        super.execute();
    }
}
