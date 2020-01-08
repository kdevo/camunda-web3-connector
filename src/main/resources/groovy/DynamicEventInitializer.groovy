package groovy

import io.github.kdevo.camunda.connect.web3.web3j_extensions.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer
import org.codehaus.groovy.control.customizers.SecureASTCustomizer

String functionString = injectedFunctionString;
boolean enableAstSecurity = injectedEnableAstSecurity;


final ImportCustomizer importCustomizer = new ImportCustomizer();
importCustomizer.addImports('org.web3j.abi.TypeReference')
importCustomizer.addStarImports(
        'org.web3j.abi.datatypes',
        'org.web3j.abi.datatypes.generated',
        'io.github.kdevo.camunda.connect.web3.web3j_extensions'
)

final conf = new CompilerConfiguration();
conf.addCompilationCustomizers(importCustomizer);


if (enableAstSecurity) {
    conf.addCompilationCustomizers(new SecureASTCustomizer(
            methodDefinitionAllowed: false,
            closuresAllowed: false,
            packageAllowed: false,
            starImportsWhitelist: ['org.web3j.abi.datatypes',
                                   'org.web3j.abi.datatypes.generated',
                                   'java.util',
                                   'java.util.Collections',
                                   'io.github.kdevo.camunda.connect.web3.web3j_extensions'],
            importsWhitelist: ['org.web3j.abi.TypeReference',
                               'Script1$1', 'Script1$2',
                               'org.codehaus.groovy.runtime.InvokerHelper'],
            staticImportsWhitelist: ['org.codehaus.groovy.runtime.InvokerHelper.runScript'],
            staticStarImportsWhitelist: ['java.util.Collections',
                                         'java.util.Arrays'],
            indirectImportCheckEnabled: true,
            statementsWhitelist: [BlockStatement, ExpressionStatement],
            expressionsWhitelist: [ListExpression, ConstructorCallExpression,
                                   ArgumentListExpression, TupleExpression, ConstantExpression,
                                   VariableExpression, ClassExpression, MethodCallExpression]
    ));
}


final shell = new GroovyShell(conf);
return (EventFilter) shell.evaluate(functionString);

