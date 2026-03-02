import { ElMessageBox } from 'element-plus'

export const useMessage = () => {
    const showWarningMessage = (message: string, title: string, confirmText: string) => {
        ElMessageBox.alert(message, title, {
            confirmButtonText: confirmText,
            type: 'warning',
            showClose: false,
        }).then()
    }

    return { showWarningMessage }
}
