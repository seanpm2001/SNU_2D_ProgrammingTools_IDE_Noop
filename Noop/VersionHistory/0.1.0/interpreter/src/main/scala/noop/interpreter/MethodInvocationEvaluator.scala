/**
 * Copyright 2009 Google Inc.
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
package noop.interpreter;

import collection.mutable.Stack;
import org.slf4j.LoggerFactory;

import model.{MethodInvocationExpression, Visitor};
import types.{NoopObject, NoopType};

/**
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class MethodInvocationEvaluator(methodInvocationExpression: MethodInvocationExpression,
    visitor: Visitor) {
  val logger = LoggerFactory.getLogger(this.getClass);

  def createFrame(context: Context, thisRef: NoopObject): Frame = {
    val method = thisRef.classDef.findMethod(methodInvocationExpression.name);

    return new Frame(thisRef, method);
  }

  def execute(context: Context) = {
    val currentFrame = context.stack.top;
    val arguments = new Stack[NoopObject]();

    logger.trace("calling method {}, eval stack is {}", methodInvocationExpression.name,
        currentFrame.lastEvaluated);
    for (i <- 0 until methodInvocationExpression.arguments.size) {
      arguments.push(currentFrame.lastEvaluated.pop());
    }
    val thisRef = currentFrame.lastEvaluated.pop();
    val frame = createFrame(context, thisRef);
    val method = frame.method;

    if (method.parameters.size != arguments.size) {
      throw new RuntimeException("Method " + method.name + " takes " + method.parameters.size +
          " arguments but " + arguments.size + " were provided");
    }
    context.stack.push(frame);
    try {
      context.stack.top.blockScopes.inScope("method " + method) {
        for (i <- 0 until method.parameters.size) {
          val value = arguments.pop();
          val identifier = method.parameters(i).name;

          frame.addIdentifier(identifier, new Tuple2[NoopType, NoopObject](null, value));
        }
        visitor.visit(method);
      }
    } finally {
      removeStackFrame(context);
    }
  }

  def removeStackFrame(context: Context) = {
    val oldFrame = context.stack.pop();

    context.stack.top.lastEvaluated ++= oldFrame.lastEvaluated;
  }
}
