package com.jeesite.modules.algorithm.utils;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

public class SpElUtil {

    public static String parseSpElTemplate(String exp, Map<String,Object> variableMap){
        // 表达式解析器
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(variableMap);

        TemplateParserContext templateParserContext = new TemplateParserContext("#{", "}");
        Expression expression = parser.parseExpression(exp, templateParserContext);
        return expression.getValue(context, String.class);

    }





}
