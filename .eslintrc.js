module.exports = {
    "env": {
        "browser": true,
        "node": true
    },
    "extends": [
        "plugin:@typescript-eslint/recommended",
        "plugin:@typescript-eslint/recommended-requiring-type-checking"
    ],
    "parser": "@typescript-eslint/parser",
    "parserOptions": {
        "project": "tsconfig.json",
        "sourceType": "module"
    },
    "plugins": [
        "@typescript-eslint",
        "@typescript-eslint/tslint"
    ],
    "rules": {
        "@typescript-eslint/array-type": "off",
        "@typescript-eslint/indent": "off",
        "@typescript-eslint/member-delimiter-style": [
            "off",
            {
                "multiline": {
                    "delimiter": "none",
                    "requireLast": true
                },
                "singleline": {
                    "delimiter": "semi",
                    "requireLast": false
                }
            }
        ],
        "@typescript-eslint/no-explicit-any": "off",
        "@typescript-eslint/no-floating-promises": "error",
        "@typescript-eslint/no-non-null-assertion": "error",
        "@typescript-eslint/no-param-reassign": "error",
        "@typescript-eslint/no-parameter-properties": "error",
        "@typescript-eslint/no-require-imports": "error",
        "@typescript-eslint/no-unnecessary-qualifier": "error",
        "@typescript-eslint/no-use-before-define": "off",
        "@typescript-eslint/prefer-for-of": "error",
        "@typescript-eslint/prefer-function-type": "error",
        "@typescript-eslint/prefer-readonly": "error",
        "@typescript-eslint/quotes": "error",
        "@typescript-eslint/restrict-plus-operands": "error",
        "@typescript-eslint/semi": [
            "off",
            null
        ],
        "@typescript-eslint/unified-signatures": "error",
        "camelcase": "error",
        "comma-dangle": "error",
        "complexity": "off",
        "constructor-super": "error",
        "curly": [
            "error",
            "multi-line"
        ],
        "default-case": "error",
        "dot-notation": "error",
        "eol-last": "error",
        "eqeqeq": [
            "error",
            "smart"
        ],
        "guard-for-in": "error",
        "id-blacklist": [
            "error",
            "any",
            "Number",
            "number",
            "String",
            "string",
            "Boolean",
            "boolean",
            "Undefined",
            "undefined"
        ],
        "id-match": "error",
        "import/no-default-export": "error",
        "import/no-deprecated": "error",
        "import/no-extraneous-dependencies": "error",
        "import/no-unassigned-import": "error",
        "jsdoc/no-types": "error",
        "linebreak-style": "error",
        "max-classes-per-file": [
            "error",
            1
        ],
        "new-parens": "error",
        "no-bitwise": "error",
        "no-caller": "error",
        "no-cond-assign": "error",
        "no-console": "off",
        "no-debugger": "error",
        "no-duplicate-case": "error",
        "no-duplicate-imports": "error",
        "no-empty": "error",
        "no-eval": "error",
        "no-fallthrough": "off",
        "no-invalid-this": "error",
        "no-irregular-whitespace": "error",
        "no-multiple-empty-lines": "error",
        "no-new-wrappers": "error",
        "no-null/no-null": "error",
        "no-redeclare": "error",
        "no-shadow": [
            "error",
            {
                "hoist": "all"
            }
        ],
        "no-sparse-arrays": "error",
        "no-template-curly-in-string": "error",
        "no-throw-literal": "error",
        "no-trailing-spaces": "error",
        "no-undef-init": "error",
        "no-underscore-dangle": "error",
        "no-unsafe-finally": "error",
        "no-unused-expressions": "error",
        "no-unused-labels": "error",
        "object-shorthand": "error",
        "one-var": [
            "error",
            "never"
        ],
        "prefer-arrow/prefer-arrow-functions": "error",
        "radix": "off",
        "spaced-comment": [
            "error",
            "never"
        ],
        "use-isnan": "error",
        "valid-typeof": "error",
        "@typescript-eslint/tslint/config": [
            "error",
            {
                "rules": {
                    "encoding": true,
                    "jsdoc-format": true,
                    "no-mergeable-namespace": true,
                    "no-reference-import": true,
                    "no-unnecessary-callback-wrapper": true,
                    "no-unsafe-any": true,
                    "return-undefined": true,
                    "switch-final-break": true,
                    "typedef": true,
                    "whitespace": true
                }
            }
        ]
    }
};
