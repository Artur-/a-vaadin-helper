import { nodeResolve } from '@rollup/plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';

export default {
  input: './rollup-index',
  output: {
    file: 'src/main/resources/META-INF/resources/frontend/a-vaadin-helper-bundle.js',
    format: 'cjs',
    exports: 'named',
  },
  plugins: [nodeResolve(), commonjs()],
};
