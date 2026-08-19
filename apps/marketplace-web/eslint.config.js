import js from '@eslint/js'
import pluginVue from 'eslint-plugin-vue'
import tseslint from '@vue/eslint-config-typescript'

export default [
  { ignores: ['dist/**', 'coverage/**'] },
  js.configs.recommended,
  ...pluginVue.configs['flat/recommended'],
  ...tseslint(),
  {
    rules: {
      'vue/multi-word-component-names': 'off',
      'vue/html-indent': 'off',
      'vue/max-attributes-per-line': 'off',
      'vue/multiline-html-element-content-newline': 'off',
      'vue/singleline-html-element-content-newline': 'off',
      'vue/mustache-interpolation-spacing': 'off'
    }
  }
]
