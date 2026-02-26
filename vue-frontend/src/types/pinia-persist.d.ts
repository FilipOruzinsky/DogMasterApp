import 'pinia'
import type { PersistedStateOptions } from 'pinia-plugin-persistedstate'

declare module 'pinia' {
    // noinspection JSUnusedGlobalSymbols
    export interface DefineStoreOptionsBase<S, Store> {
        persist?: boolean | PersistedStateOptions
    }
}
