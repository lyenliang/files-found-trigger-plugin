/*
 * The MIT License
 * 
 * Copyright (c) 2011 Steven G. Brown
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.filesfoundtrigger;

import static hudson.plugins.filesfoundtrigger.Support.SPEC;
import static hudson.plugins.filesfoundtrigger.Support.config;
import static hudson.plugins.filesfoundtrigger.Support.trigger;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import hudson.ExtensionList;
import hudson.model.Descriptor;
import hudson.model.FreeStyleProject;
import hudson.model.Hudson;

import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Integration test for the Files Found Trigger plugin. Each of the test methods
 * is executed inside a separate Jenkins instance.
 * 
 * @author Steven G. Brown
 */
public class FilesFoundTriggerIntegrationTest extends HudsonTestCase {

  /**
   */
  @SuppressWarnings("deprecation")
  @Override
  public void setUp() throws Exception {
    super.setUp();
    @SuppressWarnings("unchecked")
    ExtensionList<Descriptor> descriptors = Hudson.getInstance()
        .getExtensionList(Descriptor.class);
    descriptors.add(new FilesFoundTrigger.DescriptorImpl());
    descriptors.add(new FilesFoundTriggerConfig.DescriptorImpl());
  }

  /**
   */
  public void testGetConfigClassDescriptor() {
    assertThat(FilesFoundTriggerConfig.getClassDescriptor(),
        is(FilesFoundTriggerConfig.DescriptorImpl.class));
  }

  /**
   */
  public void testGetConfigInstanceDescriptor() {
    assertThat(config().getDescriptor(),
        is(FilesFoundTriggerConfig.DescriptorImpl.class));
  }

  /**
   * <pre>
   * http://wiki.jenkins-ci.org/display/JENKINS/Unit+Test#UnitTest-Configurationroundtriptesting
   * </pre>
   * 
   * @throws Exception
   *           on error
   */
  public void testSave() throws Exception {
    FreeStyleProject project = createFreeStyleProject();
    FilesFoundTrigger before = trigger(SPEC, config());
    project.addTrigger(before);

    submit(createWebClient().getPage(project, "configure").getFormByName(
        "config"));

    FilesFoundTrigger after = project.getTrigger(FilesFoundTrigger.class);

    assertThat(String.valueOf(after), is(String.valueOf(before)));
  }
}
