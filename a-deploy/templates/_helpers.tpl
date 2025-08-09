{{- define "mychart.fullname" -}}
{{- printf "%s-%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*metadata:*/}}
{{/*  name: {{ include "mychart.fullname" . }}*/}}


{{- define "mychart.labels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{/*metadata:*/}}
{{/*  labels:*/}}
{{/*    {{ include "mychart.labels" . | nindent 4 }}*/}}


{{- define "mychart.selectorLabels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/*spec:*/}}
{{/*  selector:*/}}
{{/*    matchLabels:*/}}
{{/*      {{ include "mychart.selectorLabels" . | nindent 6 }}*/}}


{{- define "mychart.image" -}}
{{ .Values.image.repository }}:{{ default .Chart.AppVersion .Values.image.tag }}
{{- end -}}

{{/*spec:*/}}
{{/*  containers:*/}}
{{/*    - name: {{ .Chart.Name }}*/}}
{{/*      image: {{ include "mychart.image" . }}*/}}

