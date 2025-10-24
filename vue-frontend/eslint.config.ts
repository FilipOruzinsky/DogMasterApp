import { globalIgnores } from 'eslint/config'
import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript'
import pluginVue from 'eslint-plugin-vue'
import pluginVitest from '@vitest/eslint-plugin'
import pluginPlaywright from 'eslint-plugin-playwright'
import skipFormatting from '@vue/eslint-config-prettier/skip-formatting'

// To allow more languages other than `ts` in `.vue` files, uncomment the following lines:
// import { configureVueProject } from '@vue/eslint-config-typescript'
// configureVueProject({ scriptLangs: ['ts', 'tsx'] })
// More info at https://github.com/vuejs/eslint-config-typescript/#advanced-setup

export default defineConfigWithVueTs(
  {
    name: 'app/files-to-lint',
    files: ['**/*.{ts,mts,tsx,vue}'],
  },

  globalIgnores(['**/dist/**', '**/dist-ssr/**', '**/coverage/**']),

  pluginVue.configs['flat/essential'],
  vueTsConfigs.recommended,

  {
    ...pluginVitest.configs.recommended,
    files: ['src/**/__tests__/*'],
  },

  {
    ...pluginPlaywright.configs['flat/recommended'],
    files: ['e2e/**/*.{test,spec}.{js,ts,jsx,tsx}'],
  },
  skipFormatting,
    // 🔽 Custom rules go here
    {
        rules: {
            // Set 4-space indentation
            indent: ['error', 4],
            // Enforce Vue template indentation to match
            'vue/html-indent': ['error', 4],
            // Enforce no semicolons
            semi: ['error', 'never'],
            // Prevent console.log and similar statements
            'no-console': 'error',
            // Enforce single quotes for strings
            quotes: ['error', 'single'],
            // Require spaces inside curly braces: { example }
            'object-curly-spacing': ['error', 'always'],
            // No spaces inside array brackets: [1, 2]
            'array-bracket-spacing': ['error', 'never'],
            // Require trailing commas in multiline statements
            'comma-dangle': ['error', 'always-multiline'],
            'vue/html-closing-bracket-spacing': [
                'error',
                {
                    startTag: 'never',
                    endTag: 'never',
                    selfClosingTag: 'always',
                },
            ],
        },
    },
)
